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
import com.procurement.notice.model.tender.record.Release
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
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val releaseEV: Release = releaseService.getRelease(recordEntity.jsonData)

        val cancelledCAN: CancelCANsAndContractRequest.CancelledCAN = request.cancelledCan

        /** BR-2.8.3.4 */
        val releaseId = releaseService.newReleaseId(ocid)
        val amendment: Amendment = cancelledCAN.createAmendment(releaseEV, releaseId, releaseDate)

        val updatedReleaseEV = releaseEV.copy(
            /** BR-2.8.3.1 */
            tag = listOf(Tag.AWARD_CANCELLATION),
            /** BR-2.8.3.3 */
            date = releaseDate,
            /** BR-2.8.3.4 */
            id = releaseId,
            /** BR-2.8.3.8 */
            contracts = releaseEV.contracts
                .updateContracts(
                    cancelledCAN = cancelledCAN,
                    relatedCANs = request.relatedCANs,
                    amendment = amendment
                ),
            /** BR-2.8.3.12 */
            tender = releaseEV.tender.copy(
                /** BR-2.8.3.13 */
                lots = releaseEV.tender.lots.updateLots(request.lot)
            ),

            bids = releaseEV.bids?.copy(
                /** BR-2.8.3.6 */
                details = releaseEV.bids?.details?.updateBids(request.bids)
            ),
            awards = releaseEV.awards.updateAwards(request.awards)
        )

        val contractId = request.contract.id
        val recordACEntity = releaseService.getRecordEntity(cpId = cpid, ocId = contractId)
        val recordAC = toObject(ContractRecord::class.java, recordACEntity.jsonData)

        val updatedRecordAC = recordAC.copy(
            id = releaseService.newReleaseId(contractId),
            tag = listOf(Tag.CONTRACT_TERMINATION),
            date = releaseDate,
            contracts = recordAC.contracts?.updateContract(request.contract)
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage,
            release = updatedReleaseEV,
            publishDate = recordEntity.publishDate
        )
        releaseService.saveContractRecord(
            cpId = cpid,
            stage = "AC",
            record = updatedRecordAC,
            publishDate = recordACEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    /**
     * BR-2.8.3.9
     * BR-2.8.3.10
     * BR-2.8.3.11
     */
    private fun CancelCANsAndContractRequest.CancelledCAN.createAmendment(
        record: Release,
        releaseId: String,
        releaseDate: LocalDateTime
    ): Amendment {
        return this.amendment.let { amendment ->
            Amendment(
                /** BR-2.8.3.9 */
                id = generationService.generateAmendmentId().toString(),
                /** BR-2.8.3.10 */
                amendsReleaseID = record.id,
                /** BR-2.8.3.8 4.c */
                releaseID = releaseId,
                /** BR-2.8.3.11 */
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
                relatedLots = null
            )
        }
    }

    /**
     * BR-2.8.3.8
     */
    private fun List<Contract>.updateContracts(
        cancelledCAN: CancelCANsAndContractRequest.CancelledCAN,
        relatedCANs: List<CancelCANsAndContractRequest.RelatedCAN>?,
        amendment: Amendment
    ): List<Contract> {
        val relatedCANsById = relatedCANs?.associateBy { it.id } ?: emptyMap()

        return this.asSequence()
            .map { contract ->
                val contractId = contract.id!!
                when {
                    contractId == cancelledCAN.id -> contract.copy(
                        status = cancelledCAN.status,
                        statusDetails = cancelledCAN.statusDetails,
                        amendments = contract.amendments?.plus(amendment) ?: listOf(amendment)
                    )
                    relatedCANsById.containsKey(contractId) -> {
                        val relatedCAN = relatedCANsById.getValue(contractId)
                        contract.copy(
                            status = relatedCAN.status,
                            statusDetails = relatedCAN.statusDetails
                        )
                    }
                    else -> contract
                }
            }
            .toList()
    }

    /**
     * BR-2.8.3.13
     */
    private fun List<Lot>.updateLots(lot: CancelCANsAndContractRequest.Lot): List<Lot> {
        return this.asSequence()
            .map {
                if (it.id == lot.id)
                    it.copy(
                        status = lot.status,
                        statusDetails = lot.statusDetails
                    )
                else
                    it

            }.toList()
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
    private fun List<Award>.updateAwards(awards: List<CancelCANsAndContractRequest.Award>): List<Award> {
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
            .toList()
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