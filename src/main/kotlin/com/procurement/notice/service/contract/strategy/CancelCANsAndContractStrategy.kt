package com.procurement.notice.service.contract.strategy

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.can.CancelCANsAndContractRequest
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toObject
import java.time.LocalDateTime
import java.util.*

class CancelCANsAndContractStrategy(
    private val releaseService: ReleaseService,
    private val generationService: GenerationService
) {

    fun cancelCanAndContract(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val request = toObject(CancelCANsAndContractRequest::class.java, data)
        val cancelledCAN: CancelCANsAndContractRequest.CAN = request.cans.firstOrNull {
            it.status == "cancelled"
        } ?: throw RuntimeException("Cancelled CAN is not found.")


        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record: Record = releaseService.getRecord(recordEntity.jsonData)

        val amendment: Amendment = cancelledCAN.createAmendment(record, releaseDate)

        val updatedRecord = record.copy(
            tag = listOf(Tag.AWARD_CANCELLATION),
            date = releaseDate,
            id = releaseService.newReleaseId(ocid),
            contracts = record.contracts?.updateContracts(cancelledCAN, amendment),
            tender = record.tender.copy(
                lots = record.tender.lots?.updateLots(request.lot)
            ),

            bids = record.bids?.copy(
                details = record.bids?.details?.updateBids(request.bids)
            ),
            awards = record.awards?.updateAwards(request.awards) ?: request.createAwards()
        )

        val contractOcid = request.contract.id
        val recordContractEntity = releaseService.getRecordEntity(cpId = cpid, ocId = contractOcid)
        val recordContract = toObject(ContractRecord::class.java, recordContractEntity.jsonData)

        val updatedContract = recordContract.copy(
            id = releaseService.newReleaseId(contractOcid),
            tag = listOf(Tag.CONTRACT_TERMINATION),
            date = releaseDate,
            contracts = recordContract.contracts?.updateContract(request.contract)
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage,
            record = updatedRecord,
            publishDate = recordEntity.publishDate
        )
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = "AC",
            record = updatedContract,
            publishDate = recordContractEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    /**
     * BR-2.8.3.9
     * BR-2.8.3.10
     * BR-2.8.3.11
     */
    private fun CancelCANsAndContractRequest.CAN.createAmendment(
        record: Record,
        releaseDate: LocalDateTime
    ): Amendment {
        return this.amendment.let { amendment ->
            Amendment(
                //BR-2.8.3.9
                id = generationService.generateAmendmentId().toString(),
                //BR-2.8.3.10
                amendsReleaseID = record.id,
                //BR-2.8.3.11
                date = releaseDate,
                description = amendment.description,
                rationale = amendment.rationale,
                documents = amendment.documents?.map { document ->
                    Document(
                        id = document.id,
                        documentType = document.documentType,
                        url = document.url,
                        datePublished = document.datePublished,
                        title = document.title,
                        description = document.description,
                        dateModified = null,
                        format = null,
                        language = null,
                        relatedLots = null,
                        relatedConfirmations = null
                    )
                },
                changes = null,
                relatedLots = null,
                releaseID = null
            )
        }
    }

    /**
     * BR-2.8.3.8
     */
    private fun HashSet<Contract>.updateContracts(
        can: CancelCANsAndContractRequest.CAN,
        amendment: Amendment
    ): HashSet<Contract> {
        return this.asSequence()
            .map { contract ->
                if (contract.id == can.id) {
                    contract.copy(
                        status = can.status,
                        statusDetails = can.statusDetails,
                        amendments = contract.amendments?.plus(amendment) ?: listOf(amendment)
                    )
                } else
                    contract
            }
            .toHashSet()
    }

    /**
     * BR-2.8.3.13
     */
    private fun HashSet<Lot>.updateLots(lot: CancelCANsAndContractRequest.Lot): HashSet<Lot> {
        return this.asSequence()
            .map {
                if (it.id == lot.id)
                    it.copy(
                        status = lot.status,
                        statusDetails = lot.statusDetails
                    )
                else
                    it

            }.toHashSet()
    }

    /**
     * BR-2.8.3.6
     */
    private fun HashSet<Bid>.updateBids(bids: List<CancelCANsAndContractRequest.Bid>): HashSet<Bid> {
        val bidsById = bids.associateBy { it.id }
        return this.asSequence()
            .map { bid ->
                bidsById[bid.id]?.let {
                    bid.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                } ?: bid
            }
            .toHashSet()
    }

    /**
     * BR-2.8.3.7
     */
    private fun HashSet<Award>.updateAwards(awards: List<CancelCANsAndContractRequest.Award>): HashSet<Award> {
        val awardsById = awards.associateBy { it.id }
        return this.asSequence()
            .map { award ->
                awardsById[award.id]?.let {
                    award.copy(
                        date = it.date,
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                } ?: award
            }
            .toHashSet()
    }

    /**
     * BR-2.7.8.6
     * BR-2.7.8.7
     */
    private fun HashSet<Contract>.updateContract(contract: CancelCANsAndContractRequest.Contract): HashSet<Contract> {
        val updatedContract = this.find { it.id == contract.id }
            ?.copy(
                status = contract.status,
                statusDetails = contract.statusDetails
            )
            ?: throw ErrorException(error = ErrorType.CONTRACT_BY_ID_NOT_FOUND)

        return this.asSequence()
            .map {
                if (it.id == updatedContract.id) {
                    updatedContract
                } else
                    it
            }
            .toHashSet()
    }

    private fun CancelCANsAndContractRequest.createAwards(): HashSet<Award> {
        return this.awards.asSequence()
            .map { award ->
                Award(
                    id = award.id,
                    date = award.date,
                    relatedBid = award.relatedBid,
                    status = award.status,
                    statusDetails = award.statusDetails,
                    title = null,
                    description = null,
                    value = null,
                    suppliers = null,
                    items = null,
                    contractPeriod = null,
                    documents = null,
                    amendments = null,
                    amendment = null,
                    requirementResponses = null,
                    reviewProceedings = null,
                    relatedLots = null
                )
            }
            .toHashSet()
    }
}