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

    fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto

    fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun createCnOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun createCnOnPin(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto
}


@Service
class CreateReleaseServiceImpl(private val budgetService: BudgetService,
                               private val organizationService: OrganizationService,
                               private val relatedProcessService: RelatedProcessService,
                               private val releaseService: ReleaseService) : CreateReleaseService {

    override fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = releaseService.getMs(data)
        val params = releaseService.getParamsForCreateCnPnPin(operation, Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            ocid = cpid
            date = releaseDate
            id = releaseService.getNewReleaseId(cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            tender.statusDetails = params.statusDetails
            organizationService.processMsParties(this, checkFs)
        }
        val record = releaseService.getRecord(data)
        val ocId = releaseService.getNewOcId(cpid, stage)
        record.apply {
            date = releaseDate
            ocid = ocId
            id = releaseService.getNewReleaseId(ocId)
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
        return releaseService.getResponseDto(cpid, ocId)
    }

    override fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        ms.apply {
            id = releaseService.getNewReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
            tender.procuringEntity = prevProcuringEntity
        }
        /*record*/
        val recordEntity = releaseService.getRecordEntity(cpid, prevStage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val prOcId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val ocId = releaseService.getNewOcId(cpid, stage)
        record.apply {
            /* previous record*/
            id = releaseService.getNewReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseService.saveRecord(cpid, prevStage, record)
            /*new record*/
            ocid = ocId
            id = releaseService.getNewReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.PLANNING)
            initiationType = InitiationType.TENDER
            tender = recordTender
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            hasPreviousNotice = true
            purposeOfNotice?.isACallForCompetition = false
        }
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, RelatedProcessType.PRIOR)
        relatedProcessService.addRecordRelatedProcessToRecord(record, prOcId, cpid, RelatedProcessType.PLANNING)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, stage, record)
        return releaseService.getResponseDto(cpid, ocId)
    }

    override fun createCnOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = releaseService.getNewReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseService.getRecordEntity(cpid, prevStage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val prOcId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val ocId = releaseService.getNewOcId(cpid, stage)
        record.apply {
            /* previous record*/
            id = releaseService.getNewReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseService.saveRecord(cpid, prevStage, record)
            /*new record*/
            ocid = ocId
            id = releaseService.getNewReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER)
            initiationType = InitiationType.TENDER
            tender = recordTender
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            tender.hasEnquiries = false
            hasPreviousNotice = true
            purposeOfNotice?.isACallForCompetition = true
        }
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(record, prOcId, cpid, RelatedProcessType.PLANNING)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, stage, record)
        return releaseService.getResponseDto(cpid, ocId)
    }

    override fun createCnOnPin(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        /*dto*/
        val msTender = releaseService.getMsTender(data)
        val recordTender = releaseService.getRecordTender(data)
        /*ms*/
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = releaseService.getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = releaseService.getNewReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseService.getRecordEntity(cpid, prevStage)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val prOcId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val ocId = releaseService.getNewOcId(cpid, stage)
        record.apply {
            /* previous record*/
            id = releaseService.getNewReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseService.saveRecord(cpid, prevStage, record)
            /*new record*/
            ocid = ocId
            id = releaseService.getNewReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER)
            initiationType = InitiationType.TENDER
            tender = recordTender
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            tender.hasEnquiries = false
            hasPreviousNotice = true
            purposeOfNotice?.isACallForCompetition = true
        }
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, params.relatedProcessType)
        relatedProcessService.addRecordRelatedProcessToRecord(record, prOcId, cpid, RelatedProcessType.PRIOR)
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpid, stage, record)
        return releaseService.getResponseDto(cpid, ocId)
    }

}
