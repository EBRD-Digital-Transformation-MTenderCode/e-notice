package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class CreateReleaseService(private val budgetService: BudgetService,
                           private val organizationService: OrganizationService,
                           private val relatedProcessService: RelatedProcessService,
                           private val releaseService: ReleaseService) {

    fun createCnPnPin(cpid: String,
                      stage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode,
                      operation: Operation): ResponseDto {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = releaseService.getMs(data)
        val params = releaseService.getParamsForCreateCnPnPin(operation, Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            ocid = cpid
            date = releaseDate
            id = releaseService.newReleaseId(cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            tender.statusDetails = params.statusDetails
            organizationService.processMsParties(this, checkFs)
        }
        val record = releaseService.getRecord(data)
        val ocId = releaseService.newOcId(cpId = cpid, stage = stage)
        record.apply {
            date = releaseDate
            ocid = ocId
            id = releaseService.newReleaseId(ocId)
            tag = params.tag
            initiationType = InitiationType.TENDER
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            hasPreviousNotice = false
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = params.isACallForCompetition)
        }
        relatedProcessService.addEiFsRecordRelatedProcessToMs(ms = ms, checkFs = checkFs, ocId = ocId, processType = params.relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record = record, cpId = cpid)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = releaseDate.toDate())
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = releaseDate.toDate())
        budgetService.createEiByMs(eiIds = checkFs.ei, msCpId = cpid, dateTime = releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns = budgetBreakdowns, msCpId = cpid, dateTime = releaseDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocId))
    }

    fun createPinOnPn(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
            tender.procuringEntity = prevProcuringEntity
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val newOcId = releaseService.newOcId(cpId = cpid, stage = stage)
        val newRecord = record.copy(
                id = releaseService.newReleaseId(newOcId),
                date = releaseDate,
                tag = listOf(Tag.PLANNING),
                tender = recordTender.copy(
                        title = TenderTitle.valueOf(stage.toUpperCase()).text,
                        description = TenderDescription.valueOf(stage.toUpperCase()).text
                ),
                initiationType = InitiationType.TENDER,
                hasPreviousNotice = true,
                purposeOfNotice = PurposeOfNotice(false)
        )
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = RelatedProcessType.X_PLANNED)
        relatedProcessService.addRecordRelatedProcessToRecord(record = newRecord, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = record, publishDate = recordEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = newRecord, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
    }

    fun createCnOnPn(cpid: String,
                     ocid: String,
                     stage: String,
                     prevStage: String,
                     releaseDate: LocalDateTime,
                     data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            parties = releaseService.getPartiesWithActualPersones(msTender.procuringEntity, ms.parties)
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = record, publishDate = recordEntity.publishDate)
        val newOcId = releaseService.newOcId(cpId = cpid, stage = stage)
        val newRecord = record.copy(
                ocid = newOcId,
                id = releaseService.newReleaseId(newOcId),
                date = releaseDate,
                tag = listOf(Tag.TENDER),
                tender = recordTender.copy(
                        title = TenderTitle.valueOf(stage.toUpperCase()).text,
                        description = TenderDescription.valueOf(stage.toUpperCase()).text,
                        hasEnquiries = false
                ),
                initiationType = InitiationType.TENDER,
                hasPreviousNotice = true,
                purposeOfNotice = PurposeOfNotice(true),
                parties = null
        )
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(record = newRecord, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = newRecord, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
    }

    fun createNegotiationCnOnPn(
        cpid: String,
        ocid: String,
        stage: String,
        prevStage: String,
        operation: Operation,
        releaseDate: LocalDateTime,
        data: JsonNode
    ): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(
            stage = Stage.valueOf(stage.toUpperCase()),
            operation = operation
        )
        ms.apply {
            id = releaseService.newReleaseId(cpid) //BR-2.4.16.24
            date = releaseDate //BR-2.4.16.25
            tag = listOf(Tag.COMPILED) //BR-2.4.16.23
            tender = msTender //BR-2.4.16.19
            tender.statusDetails = params.statusDetails //BR-2.4.16.20
            tender.procuringEntity = prevProcuringEntity //BR-2.4.16.21
            tender.hasEnquiries = false
        }

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val recordPN = releaseService.getRecord(recordEntity.jsonData)
        recordPN.apply {
            id = releaseService.newReleaseId(ocid) //BR-2.4.16.16
            date = releaseDate //BR-2.4.16.15
            tag = listOf(Tag.PLANNING_UPDATE) //BR-2.4.16.13
            tender.status = TenderStatus.COMPLETE //BR-2.4.16.18
            tender.statusDetails = TenderStatusDetails.EMPTY //BR-2.4.16.18
        }
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = recordPN, publishDate = recordEntity.publishDate)

        //BR-2.4.16.6
        val newOcId = releaseService.newOcId(cpId = cpid, stage = stage)

        val recordNP = recordPN.copy(
            ocid = newOcId, //BR-2.4.16.6
            id = releaseService.newReleaseId(newOcId), //BR-2.4.16.7
            date = releaseDate, //BR-2.4.16.8
            tag = listOf(Tag.TENDER), //BR-2.4.16.3
            tender = recordTender.copy( //BR-2.4.16.11
                title = TenderTitle.valueOf(stage.toUpperCase()).text, //BR-2.4.16.9
                description = TenderDescription.valueOf(stage.toUpperCase()).text, //BR-2.4.16.10
                hasEnquiries = false
            ),
            initiationType = InitiationType.TENDER, //BR-2.4.16.5
            hasPreviousNotice = true, //BR-2.4.16.1
            purposeOfNotice = PurposeOfNotice(true), //BR-2.4.16.4
            parties = null //BR-2.4.16.12
        )
        //BR-2.4.16.26
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = params.relatedProcessType)

        //BR-2.4.16.2
        relatedProcessService.addRecordRelatedProcessToRecord(record = recordNP, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)

        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = recordNP, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
    }

    fun createCnOnPin(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = record, publishDate = recordEntity.publishDate)
        val newOcId = releaseService.newOcId(cpId = cpid, stage = stage)
        val newRecord = record.copy(
                id = releaseService.newReleaseId(newOcId),
                date = releaseDate,
                tag = listOf(Tag.TENDER),
                tender = recordTender.copy(
                        title = TenderTitle.valueOf(stage.toUpperCase()).text,
                        description = TenderDescription.valueOf(stage.toUpperCase()).text,
                        hasEnquiries = false
                ),
                initiationType = InitiationType.TENDER,
                hasPreviousNotice = true,
                purposeOfNotice = PurposeOfNotice(true)
        )
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(record = newRecord, ocId = ocid, cpId = cpid, processType = RelatedProcessType.X_PLANNED)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = newRecord, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = newOcId))
    }

}
