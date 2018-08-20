package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatus
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface UpdateReleaseService {

    fun updateCn(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 data: JsonNode): ResponseDto

    fun updatePn(cpid: String,
                 ocid: String,
                 stage: String,
                 releaseDate: LocalDateTime,
                 data: JsonNode): ResponseDto

    fun updateTenderPeriod(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto
}


@Service
class UpdateReleaseServiceImpl(private val releaseService: ReleaseService) : UpdateReleaseService {

    override fun updateCn(cpid: String,
                          ocid: String,
                          stage: String,
                          releaseDate: LocalDateTime,
                          data: JsonNode): ResponseDto {
        val msReq = releaseService.getMs(data)
        val recordTender = releaseService.getRecordTender(data)
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
        }
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val amendments = record.tender.amendments?.toMutableList() ?: mutableListOf()
        var relatedLots: Set<String>? = null
        var rationale = "General change of Contract Notice"
        val canceledLots = getCanceledLotsIds(recordTender.lots)
        if (canceledLots != null && canceledLots.isNotEmpty()) {
            relatedLots = canceledLots
            rationale = "Changing of Contract Notice due to the need of cancelling lot / lots"
        }
        amendments.add(Amendment(
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
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender = recordTender
            tender.amendments = amendments
        }
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        val amendmentsIds = amendments.asSequence().map { it.id!! }.toSet()
        return releaseService.responseDto(cpid = cpid, ocid = ocid, amendments = amendmentsIds)
    }

    override fun updatePn(cpid: String,
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
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return releaseService.responseDto(cpid = cpid, ocid = ocid)
    }

    override fun updateTenderPeriod(cpid: String,
                                    ocid: String,
                                    stage: String,
                                    releaseDate: LocalDateTime,
                                    data: JsonNode): ResponseDto {
        val recordTender = releaseService.getRecordTender(data)
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val amendments = record.tender.amendments?.toMutableList()?: mutableListOf()
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
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        val amendmentsIds = amendments.asSequence().map { it.id!! }.toSet()
        return releaseService.responseDto(cpid = cpid, ocid = ocid, amendments = amendmentsIds)
    }


    fun getCanceledLotsIds(lots: HashSet<Lot>?): Set<String>? {
        return lots?.asSequence()
                ?.filter { it.status == TenderStatus.CANCELLED }
                ?.map { it.id }
                ?.toSet()
    }

}
