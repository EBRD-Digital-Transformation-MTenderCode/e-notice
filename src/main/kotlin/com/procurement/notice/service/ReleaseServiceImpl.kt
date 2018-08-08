package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.Params
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.model.tender.record.RecordTender
import com.procurement.notice.utils.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface ReleaseService {

    fun updateCn(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun updateTenderPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto

    fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun createCnOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun createCnOnPin(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto

    fun getRecordEntity(cpId: String, stage: String, record: Record): ReleaseEntity

    fun getMSEntity(cpId: String, ms: Ms): ReleaseEntity
}


@Service
class ReleaseServiceImpl(private val releaseDao: ReleaseDao,
                         private val budgetService: BudgetService,
                         private val organizationService: OrganizationService,
                         private val relatedProcessService: RelatedProcessService) : ReleaseService {

    companion object {
        private const val SEPARATOR = "-"
        private const val TENDER_JSON = "tender"
        private const val MS = "MS"
    }


    override fun updateCn(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val msReq = toObject(Ms::class.java, data.toString())
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(ReleaseServiceImpl.TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        msReq.tender.apply {
            id = ms.tender.id
            status = ms.tender.status
            statusDetails = ms.tender.statusDetails
            hasEnquiries = ms.tender.hasEnquiries
            procuringEntity = ms.tender.procuringEntity
        }
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            planning = msReq.planning
            tender = msReq.tender
        }
        /*record*/
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        recordTender.apply {
            title = record.tender.title
            description = record.tender.description
        }
        val actualReleaseID = record.id
        val newReleaseID = getReleaseId(ocId)
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
            id = getReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender = recordTender
            tender.amendments = amendments
        }
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun updateTenderPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(ReleaseServiceImpl.TENDER_JSON)))
        /*record*/
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        val actualReleaseID = record.id
        val newReleaseID = getReleaseId(ocId)
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
            id = getReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.TENDER_AMENDMENT)
            tender.tenderPeriod = recordTender.tenderPeriod
            tender.enquiryPeriod = recordTender.enquiryPeriod
            tender.amendments = amendments
        }
        releaseDao.saveRelease(getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = toObject(Ms::class.java, data.toString())
        val params = getParamsForCreateCnPnPin(operation, Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            ocid = cpid
            date = releaseDate
            id = getReleaseId(cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            tender.statusDetails = params.statusDetails
            organizationService.processMsParties(this, checkFs)
        }
        val record = toObject(Record::class.java, data.toString())
        val ocId = getOcId(cpid, stage)
        record.apply {
            date = releaseDate
            ocid = ocId
            id = getReleaseId(ocId)
            tag = params.tag
            initiationType = InitiationType.TENDER
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            hasPreviousNotice = false
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = params.isACallForCompetition)
        }
        relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, ocId, params.relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getRecordEntity(cpid, stage, record))
        budgetService.createEiByMs(checkFs.ei, cpid, releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns, cpid, releaseDate)
        return getResponseDto(cpid, ocId)
    }

    override fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
            tender.procuringEntity = prevProcuringEntity
        }
        /*record*/
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prOcId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val ocId = getOcId(cpid, stage)
        record.apply {
            /* previous record*/
            id = getReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getRecordEntity(cpid, prevStage, record))
            /*new record*/
            ocid = ocId
            id = getReleaseId(ocId)
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
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun createCnOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prOcId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val ocId = getOcId(cpid, stage)
        record.apply {
            /* previous record*/
            id = getReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getRecordEntity(cpid, prevStage, record))
            /*new record*/
            ocid = ocId
            id = getReleaseId(ocId)
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
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun createCnOnPin(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        /*dto*/
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = getParamsForUpdateCnOnPnPin(Stage.valueOf(stage.toUpperCase()))
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = params.statusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prOcId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val ocId = getOcId(cpid, stage)
        record.apply {
            /* previous record*/
            id = getReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getRecordEntity(cpid, prevStage, record))
            /*new record*/
            ocid = ocId
            id = getReleaseId(ocId)
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
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }


    fun getParamsForCreateCnPnPin(operation: Operation, stage: Stage): Params {
        val params = Params()
        when (operation) {
            Operation.CREATE_CN -> {
                params.tag = listOf(Tag.TENDER)
                params.isACallForCompetition = true
            }
            Operation.CREATE_PN, Operation.CREATE_PIN -> {
                params.tag = listOf(Tag.PLANNING)
                params.isACallForCompetition = false
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        when (stage) {
            Stage.PS -> {
                params.statusDetails = TenderStatusDetails.PRESELECTION
                params.relatedProcessType = RelatedProcessType.X_PRESELECTION
            }
            Stage.PQ -> {
                params.statusDetails = TenderStatusDetails.PREQUALIFICATION
                params.relatedProcessType = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.PN -> {
                params.statusDetails = TenderStatusDetails.PLANNING_NOTICE
                params.relatedProcessType = RelatedProcessType.PLANNING
            }
            Stage.PIN -> {
                params.statusDetails = TenderStatusDetails.PRIOR_NOTICE
                params.relatedProcessType = RelatedProcessType.PRIOR
            }
            Stage.EV -> {
                params.statusDetails = TenderStatusDetails.EVALUATION
                params.relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    fun getParamsForUpdateCnOnPnPin(stage: Stage): Params {
        val params = Params()
        when (stage) {
            Stage.PS -> {
                params.statusDetails = TenderStatusDetails.PRESELECTION
                params.relatedProcessType = RelatedProcessType.X_PRESELECTION
            }
            Stage.PQ -> {
                params.statusDetails = TenderStatusDetails.PREQUALIFICATION
                params.relatedProcessType = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.EV -> {
                params.statusDetails = TenderStatusDetails.EVALUATION
                params.relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    override fun getRecordEntity(cpId: String, stage: String, record: Record): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return getEntity(
                cpId = cpId,
                ocId = ocId,
                releaseId = releaseId,
                stage = stage,
                json = toJson(record),
                status = record.tender.status.toString()
        )
    }

    override fun getMSEntity(cpId: String, ms: Ms): ReleaseEntity {
        val releaseId = ms.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return getEntity(
                cpId = cpId,
                ocId = cpId,
                releaseId = releaseId,
                stage = MS,
                json = toJson(ms),
                status = ms.tender.status.toString()
        )
    }

    private fun getEntity(cpId: String,
                          ocId: String,
                          releaseId: String,
                          stage: String,
                          json: String,
                          status: String): ReleaseEntity {
        return ReleaseEntity(
                cpId = cpId,
                ocId = ocId,
                releaseDate = localNowUTC().toDate(),
                releaseId = releaseId,
                stage = stage,
                jsonData = json,
                status = status
        )
    }

    private fun getOcId(cpId: String, stage: String): String {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }

}
