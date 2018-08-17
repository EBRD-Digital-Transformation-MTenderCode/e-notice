package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

interface CreateReleaseService {

    fun createCnPnPin(cpid: String,
                      stage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode,
                      operation: Operation): ResponseDto

    fun createPinOnPn(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto

    fun createCnOnPn(cpid: String,
                     ocid: String,
                     stage: String,
                     prevStage: String,
                     releaseDate: LocalDateTime,
                     data: JsonNode): ResponseDto

    fun createCnOnPin(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto
}


@Service
class CreateReleaseServiceImpl(private val budgetService: BudgetService,
                               private val organizationService: OrganizationService,
                               private val relatedProcessService: RelatedProcessService,
                               private val releaseService: ReleaseService) : CreateReleaseService {

    override fun createCnPnPin(cpid: String,
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
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        budgetService.createEiByMs(eiIds = checkFs.ei, msCpId = cpid, dateTime = releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns = budgetBreakdowns, msCpId = cpid, dateTime = releaseDate)
        return releaseService.responseDto(cpid = cpid, ocid = ocId)
    }

    override fun createPinOnPn(cpid: String,
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
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = RelatedProcessType.PRIOR)
        relatedProcessService.addRecordRelatedProcessToRecord(record = newRecord, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = newRecord)
        return releaseService.responseDto(cpid = cpid, ocid = newOcId)
    }

    override fun createCnOnPn(cpid: String,
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
            tag = listOf(Tag.COMPILED)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
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
        relatedProcessService.addRecordRelatedProcessToRecord(record = newRecord, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PLANNING)
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = newRecord)
        return releaseService.responseDto(cpid = cpid, ocid = newOcId)
    }

    override fun createCnOnPin(cpid: String,
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
        relatedProcessService.addRecordRelatedProcessToRecord(record = newRecord, ocId = ocid, cpId = cpid, processType = RelatedProcessType.PRIOR)
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = newRecord)
        return releaseService.responseDto(cpid = cpid, ocid = newOcId)
    }

}
