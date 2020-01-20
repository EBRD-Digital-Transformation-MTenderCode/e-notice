package com.procurement.notice.service.contract.strategy

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.infrastructure.dto.can.CancelCANRequest
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toObject
import java.time.LocalDateTime
import java.util.*

class CancelCANsStrategy(
    private val releaseService: ReleaseService
) {

    fun cancelCan(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val request = toObject(CancelCANRequest::class.java, data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordEV: Record = releaseService.getRecord(recordEntity.jsonData)

        val cancelledCAN: CancelCANRequest.CancelledCAN = request.cancelledCan
        val amendment: Amendment = cancelledCAN.createAmendment(recordEV, releaseDate)

        val updatedRecordEV = recordEV.copy(
            /** BR-2.8.3.1 */
            tag = listOf(Tag.AWARD_CANCELLATION),
            /** BR-2.8.3.3 */
            date = releaseDate,
            /** BR-2.8.3.4 */
            id = releaseService.newReleaseId(ocid),
            /** BR-2.8.3.8 */
            contracts = recordEV.contracts?.updateContracts(cancelledCAN, amendment),
            /** BR-2.8.3.12 */
            tender = recordEV.tender.copy(
                /** BR-2.8.3.13 */
                lots = recordEV.tender.lots?.updateLots(request.lot)
            ),

            bids = recordEV.bids?.copy(
                details = recordEV.bids?.details?.updateBids(request.bids)
            ),
            awards = recordEV.awards?.updateAwards(request.awards) ?: request.createAwards()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage,
            record = updatedRecordEV,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    /**
     * BR-2.8.3.9
     * BR-2.8.3.10
     * BR-2.8.3.11
     */
    private fun CancelCANRequest.CancelledCAN.createAmendment(record: Record, releaseDate: LocalDateTime): Amendment {
        return this.amendment.let { amendment ->
            Amendment(
                /**BR-2.8.3.9 */
                id = generationService.generateAmendmentId().toString(),
                /**BR-2.8.3.10 */
                amendsReleaseID = record.id,
                /**BR-2.8.3.11 */
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
        cancelledCAN: CancelCANRequest.CancelledCAN,
        amendment: Amendment
    ): HashSet<Contract> {
        return this.asSequence()
            .map { contract ->
                if (contract.id == cancelledCAN.id) {
                    contract.copy(
                        status = cancelledCAN.status,
                        statusDetails = cancelledCAN.statusDetails,
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
    private fun HashSet<Lot>.updateLots(lot: CancelCANRequest.Lot): HashSet<Lot> {
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
    private fun HashSet<Bid>.updateBids(bids: List<CancelCANRequest.Bid>): HashSet<Bid> {
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
    private fun HashSet<Award>.updateAwards(awards: List<CancelCANRequest.Award>): HashSet<Award> {
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

    private fun CancelCANRequest.createAwards(): HashSet<Award> {
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