package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.tender.cancel.CancelStandStillPeriodContext
import com.procurement.notice.application.service.tender.cancel.CancelStandStillPeriodData
import com.procurement.notice.application.service.tender.cancel.CancelledStandStillPeriodData
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.dto.CancellationStandstillPeriodDto
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

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
            id = releaseService.newReleaseId(cpid),
            date = context.releaseDate,
            tag = listOf(Tag.TENDER_CANCELLATION),
            tender = ms.tender.copy(
                statusDetails = TenderStatusDetails.STANDSTILL
            )
        )

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        //BR-2.4.8.10
        val newAmendment = newAmendment(context, data, record)
        val amendments = record.tender.amendments ?: emptyList()
        val updatedAmendment = amendments + newAmendment

        val updatedRecord = record.copy(
            //BR-2.4.8.4
            id = releaseService.newReleaseId(ocid),

            //BR-2.4.8.3
            date = context.releaseDate,

            //BR-2.4.8.1
            tag = listOf(Tag.TENDER_CANCELLATION),

            //BR-2.4.8.6
            tender = record.tender.copy(
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
            record = updatedRecord,
            publishDate = recordEntity.publishDate
        )
        val amendmentsIds = amendments.map { it.id!! }
        return CancelledStandStillPeriodData(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds)
    }

    /**
     * BR-2.4.8.10
     */
    private fun newAmendment(
        context: CancelStandStillPeriodContext,
        data: CancelStandStillPeriodData,
        record: Record
    ): Amendment = Amendment(
        //BR-2.4.8.11
        id = generationService.generateAmendmentId().toString(),

        //BR-2.4.8.12
        amendsReleaseID = record.id,

        //BR-2.4.8.13
        releaseID = releaseService.newReleaseId(context.ocid),

        //BR-2.4.8.14
        date = context.releaseDate,
        relatedLots = null,
        rationale = data.amendment.rationale,
        changes = null,
        description = data.amendment.description,
        documents = data.amendment.documents?.map { document ->
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
    )

    fun tenderCancellation(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto {
        val dto = toObject(CancellationStandstillPeriodDto::class.java, toJson(data))
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.CANCELLED
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.CANCELLED
            tender.statusDetails = TenderStatusDetails.EMPTY
            if (dto.lots != null) tender.lots?.let { updateLots(it, dto.lots) }
            if (dto.bids != null) bids?.details?.let { updateBids(it, dto.bids) }
            if (dto.awards != null) {
                if (awards != null) {
                    updateAwards(awards!!, dto.awards)
                } else {
                    awards = dto.awards
                }
            }
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateAwards(recordAwards: HashSet<Award>, dtoAwards: HashSet<Award>) {
        for (award in recordAwards) {
            dtoAwards.firstOrNull { it.id == award.id }?.apply {
                award.date = this.date
                award.status = this.status
                award.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateBids(recordBids: HashSet<Bid>, dtoBids: HashSet<Bid>) {
        for (bid in recordBids) {
            dtoBids.firstOrNull { it.id == bid.id }?.apply {
                bid.date = this.date
                bid.status = this.status
                bid.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateLots(recordLots: HashSet<Lot>, dtoLots: HashSet<Lot>) {
        for (lot in recordLots) {
            dtoLots.firstOrNull { it.id == lot.id }?.apply {
                lot.status = this.status
                lot.statusDetails = this.statusDetails
            }
        }
    }
}