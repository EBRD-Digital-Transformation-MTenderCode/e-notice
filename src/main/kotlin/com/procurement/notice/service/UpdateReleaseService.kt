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

    fun updateCn(
        cpid: String,
        ocid: String,
        stage: String,
        releaseDate: LocalDateTime,
        isAuction: Boolean,
        data: JsonNode
    ): ResponseDto {
        val dto = toObject(UpdateCnDto::class.java, data)
        val requestTenderEV = releaseService.getRecordTender(data)

        val requestMS = releaseService.getMs(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val recordMS = releaseService.getMs(msEntity.jsonData)

        val updatedRecordMS = recordMS.copy(
            id = releaseService.newReleaseId(cpid), //FR-5.0.1
            date = releaseDate, //FR-5.0.2
            tag = listOf(Tag.COMPILED), //FR-MR-5.5.2.3.1
            planning = requestMS.planning, //FR-MR-5.5.2.3.6
            tender = requestMS.tender.copy(
                id = recordMS.tender.id, //FR-MR-5.5.2.3.4
                status = recordMS.tender.status, //FR-MR-5.5.2.3.4
                statusDetails = recordMS.tender.statusDetails, //FR-MR-5.5.2.3.4
                hasEnquiries = recordMS.tender.hasEnquiries,
                procuringEntity = recordMS.tender.procuringEntity //FR-MR-5.5.2.3.3
            ),

            //FR-MR-5.5.2.3.7
            parties = recordMS.parties?.map { party ->
                if (party.id == requestMS.tender.procuringEntity!!.id)
                    party.copy(
                        persones = requestMS.tender.procuringEntity!!.persones
                    )
                else
                    party
            }?.toHashSet()
        )

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordEV = releaseService.getRecord(recordEntity.jsonData)

        val actualReleaseID = recordEV.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        val requestAmendments: List<Amendment> = requestTenderEV.amendments ?: emptyList()
        val newAmendments: List<Amendment> = if (requestAmendments.isNotEmpty())
            requestAmendments.map { amendment ->
                Amendment(
                    id = UUID.randomUUID().toString(),
                    amendsReleaseID = actualReleaseID,
                    releaseID = newReleaseID,
                    date = releaseDate,
                    rationale = "Changing of Contract Notice due to the need of cancelling lot / lots",
                    relatedLots = amendment.relatedLots,
                    changes = null,
                    description = null,
                    documents = null
                )
            }
        else
            listOf(
                Amendment(
                    id = UUID.randomUUID().toString(),
                    amendsReleaseID = actualReleaseID,
                    releaseID = newReleaseID,
                    date = releaseDate,
                    rationale = "General change of Contract Notice",
                    relatedLots = null,
                    changes = null,
                    description = null,
                    documents = null
                )
            )

        val recordEVAmendments = recordEV.tender.amendments ?: emptyList()
        val updatedAmendments: List<Amendment> = recordEVAmendments.plus(newAmendments)

        val isAuctionPeriodChanged = dto.isAuctionPeriodChanged ?: false
        val updatedRecordEV = recordEV.copy(
            id = newReleaseID, //FR-5.0.1
            date = releaseDate, //FR-5.0.2
            tag = listOf(Tag.TENDER_AMENDMENT), //FR-ER-5.5.2.3.1
            tender = requestTenderEV.copy(
                title = recordEV.tender.title, //FR-ER-5.5.2.3.3
                description = recordEV.tender.description, //FR-ER-5.5.2.3.3
                enquiries = recordEV.tender.enquiries, //FR-ER-5.5.2.3.3
                hasEnquiries = recordEV.tender.hasEnquiries, //FR-ER-5.5.2.3.3
                amendments = updatedAmendments, //if (amendments.isNotEmpty()) amendments else null //FR-ER-5.5.2.3.4
                auctionPeriod = if (isAuction && isAuctionPeriodChanged)
                    requestTenderEV.auctionPeriod
                else
                    recordEV.tender.auctionPeriod,

                procurementMethodModalities = if (isAuction && isAuctionPeriodChanged)
                    requestTenderEV.procurementMethodModalities
                else
                    recordEV.tender.procurementMethodModalities,

                electronicAuctions = if (isAuction && isAuctionPeriodChanged)
                    requestTenderEV.electronicAuctions
                else
                    recordEV.tender.electronicAuctions
            )
        )

        releaseService.saveMs(cpId = cpid, ms = updatedRecordMS, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = cpid,
            stage = stage,
            record = updatedRecordEV,
            publishDate = recordEntity.publishDate
        )
        return ResponseDto(
            data = DataResponseDto(
                cpid = cpid,
                ocid = ocid,
                amendmentsIds = newAmendments.map { it.id!! }
            )
        )
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
                description = null,
                documents = null
        ))
        record.apply {
            id = newReleaseID
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender.tenderPeriod = recordTender.tenderPeriod
            tender.enquiryPeriod = recordTender.enquiryPeriod
            tender.amendments = if (amendments.isNotEmpty()) amendments else null
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        val amendmentsIds = amendments.map { it.id!! }
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }
}



