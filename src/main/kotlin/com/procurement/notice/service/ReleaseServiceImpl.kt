package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.dto.Operation
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.model.tender.record.RecordTender
import com.procurement.notice.model.tender.record.TenderDescription
import com.procurement.notice.model.tender.record.TenderTitle
import com.procurement.notice.utils.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
interface ReleaseService {

    fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto<*>

    fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun createCnOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun createCnOnPin(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun getReleaseEntity(cpId: String, stage: String, record: Record): ReleaseEntity
}


@Service
class ReleaseServiceImpl(private val releaseDao: ReleaseDao,
                         private val budgetService: BudgetService,
                         private val organizationService: OrganizationService,
                         private val relatedProcessService: RelatedProcessService) : ReleaseService {

    companion object {
        private val SEPARATOR = "-"
        private val TENDER_JSON = "tender"
        private val MS = "MS"
    }

    override fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto<*> {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = toObject(Ms::class.java, data.toString())
        val params = getParamsForCreateCnPnPinOperation(operation, Stage.valueOf(stage.toUpperCase()))
        val msStatusDetails = params["msStatusDetails"] as TenderStatusDetails
        val recordTag = params["recordTag"] as List<Tag>
        val isACallForCompetition = params["isACallForCompetition"] as Boolean
        val relatedProcessType = params["relatedProcessType"] as RelatedProcessType
        with(ms) {
            ocid = cpid
            date = releaseDate
            id = getReleaseId(cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            tender.statusDetails = msStatusDetails
            organizationService.processMsParties(ms, checkFs)
        }
        val record = toObject(Record::class.java, data.toString())
        val ocId = getOcId(cpid, stage)
        with(record) {
            date = releaseDate
            ocid = ocId
            id = getReleaseId(ocId)
            tag = recordTag
            initiationType = InitiationType.TENDER
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            tender.hasEnquiries = false
            hasPreviousNotice = false
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = isACallForCompetition)
        }
        relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, ocId, relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        budgetService.createEiByMs(checkFs.ei, cpid, releaseDate)
        val budgetBreakdowns = ms.planning?.budget?.budgetBreakdown ?: throw ErrorException(ErrorType.BREAKDOWN_ERROR)
        budgetService.createFsByMs(budgetBreakdowns, cpid, releaseDate)
        return getResponseDto(cpid, ocId)
    }

    fun getParamsForCreateCnPnPinOperation(operation: Operation, stage: Stage): HashMap<String, Any> {
        val params: HashMap<String, Any> = hashMapOf()
        when (operation) {
            Operation.CREATE_CN -> {
                params["recordTag"] = listOf(Tag.TENDER)
                params["isACallForCompetition"] = true
            }
            Operation.CREATE_PN -> {
                params["recordTag"] = listOf(Tag.PLANNING)
                params["isACallForCompetition"] = false
            }
            Operation.CREATE_PIN -> {
                params["recordTag"] = listOf(Tag.PLANNING)
                params["isACallForCompetition"] = false
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        when (stage) {
            Stage.PS -> {
                params["msStatusDetails"] = TenderStatusDetails.PRESELECTION
                params["relatedProcessType"] = RelatedProcessType.X_PRESELECTION
            }
            Stage.PQ -> {
                params["msStatusDetails"] = TenderStatusDetails.PREQUALIFICATION
                params["relatedProcessType"] = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.PN -> {
                params["msStatusDetails"] = TenderStatusDetails.PLANNING_NOTICE
                params["relatedProcessType"] = RelatedProcessType.PLANNING
            }
            Stage.PIN -> {
                params["msStatusDetails"] = TenderStatusDetails.PRIOR_NOTICE
                params["relatedProcessType"] = RelatedProcessType.PRIOR
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    override fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        with(ms) {
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
        val prOcId = record.ocid!!
        val ocId = getOcId(cpid, stage)
        with(record) {
            /* previous record*/
            id = getReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
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
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prOcId, cpid)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun createCnOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = getParamsForUpdateCnOnPnPinOperation(Stage.valueOf(stage.toUpperCase()))
        val msStatusDetails = params["msStatusDetails"] as TenderStatusDetails
        with(ms) {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = msStatusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prOcId = record.ocid!!
        val ocId = getOcId(cpid, stage)
        with(record) {
            /* previous record*/
            id = getReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
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
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, RelatedProcessType.PRIOR)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prOcId, cpid)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun createCnOnPin(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        val params = getParamsForUpdateCnOnPnPinOperation(Stage.valueOf(stage.toUpperCase()))
        val msStatusDetails = params["msStatusDetails"] as TenderStatusDetails
        with(ms) {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = msStatusDetails
            tender.procuringEntity = prevProcuringEntity
            tender.hasEnquiries = false
        }
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prOcId = record.ocid!!
        val ocId = getOcId(cpid, stage)
        with(record) {
            /* previous record*/
            id = getReleaseId(prOcId)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
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
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, RelatedProcessType.PRIOR)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prOcId, cpid)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    fun getParamsForUpdateCnOnPnPinOperation(stage: Stage): HashMap<String, Any> {
        val params: HashMap<String, Any> = hashMapOf()
        when (stage) {
            Stage.PS -> {
                params["msStatusDetails"] = TenderStatusDetails.PRESELECTION
            }
            Stage.PQ -> {
                params["msStatusDetails"] = TenderStatusDetails.PREQUALIFICATION
            }
            Stage.EV -> {
                params["msStatusDetails"] = TenderStatusDetails.EVALUATION
            }
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        return params
    }

    override fun getReleaseEntity(cpId: String, stage: String, record: Record): ReleaseEntity {
        return getEntity(
                cpId = cpId,
                ocId = record.ocid!!,
                releaseDate = record.date!!.toDate(),
                releaseId = record.id!!,
                stage = stage,
                json = toJson(record)
        )
    }

    private fun getMSEntity(cpId: String, ms: Ms): ReleaseEntity {
        return getEntity(
                cpId = cpId,
                ocId = cpId,
                releaseDate = ms.date!!.toDate(),
                releaseId = ms.id!!,
                stage = MS,
                json = toJson(ms)
        )
    }

    private fun getEntity(cpId: String,
                          ocId: String,
                          releaseDate: Date,
                          releaseId: String,
                          stage: String,
                          json: String): ReleaseEntity {
        return ReleaseEntity(
                cpId = cpId,
                ocId = ocId,
                releaseDate = releaseDate,
                releaseId = releaseId,
                stage = stage,
                jsonData = json
        )
    }

    private fun getOcId(cpId: String, stage: String): String {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }

}
