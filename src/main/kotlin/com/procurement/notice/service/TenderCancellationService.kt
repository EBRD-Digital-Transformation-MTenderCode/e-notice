package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.tender.cancel.CancelStandStillPeriodContext
import com.procurement.notice.application.service.tender.cancel.CancelStandStillPeriodData
import com.procurement.notice.application.service.tender.cancel.CancelledStandStillPeriodData
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.ReleaseAmendment
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.dto.CancellationStandstillPeriodDto
import com.procurement.notice.model.tender.record.Release
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TenderCancellationService(
    private val releaseService: ReleaseService,
    private val generationService: GenerationService
) {

    fun cancellationStandstillPeriod(
        context: CancelStandStillPeriodContext,
        data: CancelStandStillPeriodData
    ): CancelledStandStillPeriodData {
        val cpid = context.cpid
        val ocid = context.ocid

        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)

        val updatedMs = ms.copy(
            id = generationService.generateReleaseId(cpid),
            date = context.releaseDate,
            tag = listOf(Tag.TENDER_CANCELLATION),
            tender = ms.tender.copy(
                statusDetails = TenderStatusDetails.STANDSTILL
            )
        )

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        //BR-2.4.8.10
        val newAmendments: List<ReleaseAmendment> = newAmendments(context, data, release)
        val amendments = release.tender.amendments
        val updatedAmendment = amendments + newAmendments

        val updatedRecord = release.copy(
            //BR-2.4.8.4
            id = generationService.generateReleaseId(ocid),

            //BR-2.4.8.3
            date = context.releaseDate,

            //BR-2.4.8.1
            tag = listOf(Tag.TENDER_CANCELLATION),

            //BR-2.4.8.6
            tender = release.tender.copy(
                //BR-2.4.8.9
                statusDetails = TenderStatusDetails.CANCELLATION,

                //BR-2.4.8.7
                standstillPeriod = data.standstillPeriod.let { period ->
                    Period(
                        startDate = period.startDate,
                        endDate = period.endDate,
                        durationInDays = null,
                        maxExtentDate = null
                    )
                },
                amendments = updatedAmendment
            )
        )

        releaseService.saveMs(cpId = cpid, ms = updatedMs, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = cpid,
            stage = context.stage,
            release = updatedRecord,
            publishDate = recordEntity.publishDate
        )
        val amendmentsIds = newAmendments.map { it.id!! }
        return CancelledStandStillPeriodData(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds)
    }

    /**
     * BR-2.4.8.10
     */
    private fun newAmendments(
        context: CancelStandStillPeriodContext,
        data: CancelStandStillPeriodData,
        release: Release
    ): List<ReleaseAmendment> = data.amendments.map { amendment ->
        ReleaseAmendment(
            //BR-2.4.8.11
            id = generationService.generateAmendmentId().toString(),

            //BR-2.4.8.12
            amendsReleaseID = release.id,

            //BR-2.4.8.13
            releaseID = generationService.generateReleaseId(context.ocid),

            //BR-2.4.8.14
            date = context.releaseDate,
            relatedLots = emptyList(),
            rationale = amendment.rationale,
            changes = emptyList(),
            description = amendment.description,
            documents = amendment.documents?.map { document ->
                Document(
                    id = document.id,
                    documentType = document.documentType,
                    title = document.title,
                    description = document.description,
                    datePublished = document.datePublished,
                    url = document.url,
                    dateModified = null,
                    format = null,
                    language = null,
                    relatedLots = null,
                    relatedConfirmations = null
                )
            }
                .orEmpty(),
            type = null,
            status = null,
            relatedItem = null,
            relatesTo = null
        )
    }

    fun tenderCancellation(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(CancellationStandstillPeriodDto::class.java, toJson(data))
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = generationService.generateReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.CANCELLED
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.CANCELLED
            tender.statusDetails = TenderStatusDetails.EMPTY
            if (dto.lots != null) tender.lots.let { updateLots(it, dto.lots) }
            if (dto.bids != null) bids?.details?.let { updateBids(it, dto.bids) }
            if (dto.awards.isNotEmpty()) {
                if (awards.isNotEmpty()) {
                    awards = updateAwards(awards, dto.awards)
                } else {
                    awards = dto.awards
                }
            }
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, awardsIds = dto.awards.map { it.id!! }))
    }

    private fun updateAwards(persistAwards: List<Award>, requestAwards: List<Award>): List<Award> {
        val persistAwardsById: Map<String, Award> = persistAwards.associateBy { it.id!! }
        val requestAwardsByIds: Map<String, Award> = requestAwards.associateBy { it.id!! }
        val allAwardIds: Set<String> = persistAwardsById.keys + requestAwardsByIds.keys

        return allAwardIds.map { awardId ->
            requestAwardsByIds[awardId]
                ?.let { requestAward ->
                    persistAwardsById[awardId]
                        ?.copy(
                            date = requestAward.date,
                            status = requestAward.status,
                            statusDetails = requestAward.statusDetails
                        )
                        ?: Award(
                            id = requestAward.id,
                            date = requestAward.date,
                            relatedBid = requestAward.relatedBid,
                            status = requestAward.status,
                            statusDetails = requestAward.statusDetails,
                            title = requestAward.title,
                            description = requestAward.description,
                            value = requestAward.value,
                            suppliers = requestAward.suppliers,
                            items = requestAward.items,
                            contractPeriod = requestAward.contractPeriod,
                            documents = requestAward.documents,
                            amendments = requestAward.amendments,
                            amendment = requestAward.amendment,
                            requirementResponses = requestAward.requirementResponses,
                            reviewProceedings = requestAward.reviewProceedings,
                            relatedLots = requestAward.relatedLots
                        )
                }
                ?: persistAwardsById.getValue(awardId)
        }
    }

    private fun updateBids(recordBids: List<Bid>, dtoBids: List<Bid>) {
        for (bid in recordBids) {
            dtoBids.firstOrNull { it.id == bid.id }?.apply {
                bid.date = this.date
                bid.status = this.status
                bid.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateLots(recordLots: List<Lot>, dtoLots: List<Lot>) {
        for (lot in recordLots) {
            dtoLots.firstOrNull { it.id == lot.id }?.apply {
                lot.status = this.status
                lot.statusDetails = this.statusDetails
            }
        }
    }
}