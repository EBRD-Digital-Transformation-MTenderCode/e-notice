package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatusDetails
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface UpdateReleaseService {

    fun updateCn(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun updatePn(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun updateTenderPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto
}


@Service
class UpdateReleaseServiceImpl(private val releaseService: ReleaseService) : UpdateReleaseService {

    override fun updateCn(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val msReq = releaseService.getMs(data)
        val recordTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        msReq.tender.apply {
            id = ms.tender.id
            status = ms.tender.status
            statusDetails = ms.tender.statusDetails
            hasEnquiries = ms.tender.hasEnquiries
            procuringEntity = ms.tender.procuringEntity
        }
        ms.apply {
            id = releaseService.getNewReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        /*record*/
        val recordEntity = releaseService.getRecordEntity(cpid, stage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val ocId = record.ocid!!
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
            enquiries = record.tender.enquiries
            hasEnquiries = record.tender.hasEnquiries
        }
        val actualReleaseID = record.id
        val newReleaseID = releaseService.getNewReleaseId(ocId)
        val amendments = record.tender.amendments?.toMutableList()
        var relatedLots: Set<String>? = null
        var rationale = "General change of Contract Notice"
        val canceledLots = recordTender.lots?.asSequence()
                ?.filter { it.statusDetails == TenderStatusDetails.CANCELLED }
                ?.map { it.id }
                ?.toSet()
        if (canceledLots != null && canceledLots.isNotEmpty()) {
            relatedLots = canceledLots
            rationale = "Changing of Contract Notice due to the need of cancelling lot / lots"
        }
        amendments?.add(Amendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = relatedLots,
                rationale = rationale,
                changes = null,
                description = null
        ))
        record.apply {
            /* previous record*/
            id = releaseService.getNewReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender = recordTender
            tender.amendments = amendments
        }
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, stage, record)
        return releaseService.getResponseDto(cpid, ocId)
    }

    override fun updatePn(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
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
        }
        ms.apply {
            id = releaseService.getNewReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        /*record*/
        val recordEntity = releaseService.getRecordEntity(cpid, stage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val ocId = record.ocid!!
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
        }
        val actualReleaseID = record.id
        val newReleaseID = releaseService.getNewReleaseId(ocId)
        val amendments = record.tender.amendments?.toMutableList()
        var relatedLots: Set<String>? = null
        var rationale = "General change of Planning Notice"
        val canceledLots = recordTender.lots?.asSequence()
                ?.filter { it.statusDetails == TenderStatusDetails.CANCELLED }
                ?.map { it.id }
                ?.toSet()
        if (canceledLots != null && canceledLots.isNotEmpty()) {
            relatedLots = canceledLots
            rationale = "Changing of Planning Notice due to the need of cancelling lot / lots"
        }
        amendments?.add(Amendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = relatedLots,
                rationale = rationale,
                changes = null,
                description = null
        ))
        record.apply {
            /* previous record*/
            id = releaseService.getNewReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender = recordTender
            tender.amendments = amendments
        }
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, stage, record)
        return releaseService.getResponseDto(cpid, ocId)
    }

    override fun updateTenderPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val recordTender = releaseService.getRecordTender(data)
        /*record*/
        val recordEntity = releaseService.getRecordEntity(cpid, stage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val ocId = record.ocid!!
        val actualReleaseID = record.id
        val newReleaseID = releaseService.getNewReleaseId(ocId)
        val amendments = record.tender.amendments?.toMutableList()
        amendments?.add(Amendment(
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
            id = releaseService.getNewReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender.tenderPeriod = recordTender.tenderPeriod
            tender.enquiryPeriod = recordTender.enquiryPeriod
            tender.amendments = amendments
        }
        releaseService.saveRecord(cpid, stage, record)
        return releaseService.getResponseDto(cpid, ocId)
    }

}
