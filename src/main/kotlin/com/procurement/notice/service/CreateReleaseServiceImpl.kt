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
        val ocId = releaseService.newOcId(cpid, stage)
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
        relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, ocId, params.relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, stage, record)
        budgetService.createEiByMs(checkFs.ei, cpid, releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns, cpid, releaseDate)
        return releaseService.responseDto(cpid, ocId)
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
        val recordEntity = releaseService.getRecordEntity(cpid, ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val newOcId = releaseService.newOcId(cpid, stage)
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
        relatedProcessService.addRecordRelatedProcessToMs(ms, newOcId, RelatedProcessType.PRIOR)
        relatedProcessService.addRecordRelatedProcessToRecord(record, ocid, cpid, RelatedProcessType.PLANNING)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, prevStage, record)
        releaseService.saveRecord(cpid, stage, newRecord)
        return releaseService.responseDto(cpid, newOcId)
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
        val recordEntity = releaseService.getRecordEntity(cpid, prevStage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val newOcId = releaseService.newOcId(cpid, stage)
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
        relatedProcessService.addRecordRelatedProcessToMs(ms, newOcId, params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(record, ocid, cpid, RelatedProcessType.PLANNING)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, prevStage, record)
        releaseService.saveRecord(cpid, stage, newRecord)
        return releaseService.responseDto(cpid, newOcId)
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
        val recordEntity = releaseService.getRecordEntity(cpid, prevStage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            /* previous record*/
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val newOcId = releaseService.newOcId(cpid, stage)
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
        relatedProcessService.addRecordRelatedProcessToMs(ms, newOcId, params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(record, ocid, cpid, RelatedProcessType.PRIOR)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, prevStage, record)
        releaseService.saveRecord(cpid, stage, newRecord)
        return releaseService.responseDto(cpid, newOcId)
    }

}
