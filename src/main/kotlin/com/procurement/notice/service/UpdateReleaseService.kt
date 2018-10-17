package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.dto.UpdateCnDto
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class UpdateReleaseService(private val releaseService: ReleaseService) {

    fun updateCn(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 isAuction: Boolean,
                 data: JsonNode): ResponseDto {
        val msReq = releaseService.getMs(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val dto = toObject(UpdateCnDto::class.java, data)
        msReq.tender.apply {
            id = ms.tender.id
            status = ms.tender.status
            statusDetails = ms.tender.statusDetails
            hasEnquiries = ms.tender.hasEnquiries
            procuringEntity = ms.tender.procuringEntity
        }
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
            enquiries = record.tender.enquiries
            hasEnquiries = record.tender.hasEnquiries
            if (!isAuction) {
                auctionPeriod = record.tender.auctionPeriod
                procurementMethodModalities = record.tender.procurementMethodModalities
                electronicAuctions = record.tender.electronicAuctions
            }
        }
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val amendments = record.tender.amendments?.toMutableList() ?: mutableListOf()
        var rationale: String
        val canceledLots = dto.amendment?.relatedLots
        rationale = if (canceledLots != null) {
            "Changing of Contract Notice due to the need of cancelling lot / lots"
        } else {
            "General change of Contract Notice"
        }
        amendments.add(Amendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = canceledLots,
                rationale = rationale,
                changes = null,
                description = null
        ))
        record.apply {
            /* previous record*/
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender = recordTender
            tender.amendments = amendments
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        val amendmentsIds = amendments.asSequence().map { it.id!! }.toSet()
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }

    fun updatePn(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 data: JsonNode): ResponseDto {
        val msReq = releaseService.getMs(data)
        val recordTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        msReq.tender.apply {
            id = ms.tender.id
            status = ms.tender.status
            statusDetails = ms.tender.statusDetails
            procuringEntity = ms.tender.procuringEntity
            hasEnquiries = ms.tender.hasEnquiries
        }
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
        }
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender = recordTender
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        val amendmentsIds = null
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }

    fun updateTenderPeriod(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto {
        val recordTender = releaseService.getRecordTender(data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val amendments = record.tender.amendments?.toMutableList() ?: mutableListOf()
        amendments.add(Amendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = null,
                rationale = "Extension of tender period",
                changes = null,
                description = null
        ))
        record.apply {
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender.tenderPeriod = recordTender.tenderPeriod
            tender.enquiryPeriod = recordTender.enquiryPeriod
            tender.amendments = amendments
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        val amendmentsIds = amendments.asSequence().map { it.id!! }.toSet()
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }
}
