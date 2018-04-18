package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.*
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

    fun getReleaseEntity(cpId: String, stage: String, record: Record): ReleaseEntity

    fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto<*>

    fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun standstillPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun startNewStage(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>
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

    override fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode, operation: Operation): ResponseDto<*> {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = toObject(Ms::class.java, data.toString())
        val params = getParamsForOperation(operation, Stage.valueOf(stage.toUpperCase()))
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

    fun getParamsForOperation(operation: Operation, stage: Stage): HashMap<String, Any> {
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

    override fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(TenderPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        with(record) {
            id = getReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.AWARD)
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = dto.awards
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        if (Stage.valueOf(stage.toUpperCase()) == Stage.PS)
            organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(SuspendTenderDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        with(record) {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = dto.tender.statusDetails
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardByBidDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        with(record) {
            id = getReleaseId(ocId)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        with(record) {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = dto.awards
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun standstillPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val dto = toObject(StandstillPeriodEndDto::class.java, toJson(data))
        val statusDetails = when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PS -> {
                TenderStatusDetails.PRESELECTED
            }
            Stage.PQ -> {
                TenderStatusDetails.PREQUALIFIED
            }
            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }

        /*MS*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        with(ms) {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        /*Record*/
        val releaseEntity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, releaseEntity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        with(record) {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = statusDetails
            tender.standstillPeriod = dto.standstillPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun startNewStage(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val dto = toObject(StartNewStageDto::class.java, toJson(data))
        var statusDetails: TenderStatusDetails?
        var relatedProcessType: RelatedProcessType?
        when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PQ -> {
                statusDetails = TenderStatusDetails.PREQUALIFICATION
                relatedProcessType = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.EV -> {
                statusDetails = TenderStatusDetails.EVALUATION
                relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }
        /*Multi stage*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        with(ms) {
            id = getReleaseId(ms.ocid!!)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        /*prev record*/
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val prevRecord = toObject(Record::class.java, recordEntity.jsonData)
        val prOcId = prevRecord.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        with(prevRecord) {
            id = getReleaseId(prOcId)
            date = releaseDate
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getReleaseEntity(cpid, prevStage, prevRecord))
        }
        /*new record*/
        val ocId = getOcId(cpid, stage)
        val record = Record(
                ocid = ocId,
                id = getReleaseId(ocId),
                date = releaseDate,
                tag = listOf(Tag.COMPILED),
                initiationType = InitiationType.TENDER,
                parties = null,
                tender = dto.tender,
                awards = null,
                bids = dto.bids,
                hasPreviousNotice = prevRecord.hasPreviousNotice,
                purposeOfNotice = prevRecord.purposeOfNotice,
                relatedProcesses = null)
        processDocuments(record, dto)
        organizationService.processRecordPartiesFromBids(record)
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prOcId, cpid)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*Multi stage*/
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

    private fun processDocuments(record: Record, dto: StartNewStageDto) {
        if (dto.tender.documents != null) {
            val docIds = dto.tender.documents!!.asSequence().map { it.id!! }.toSet()
            record.tender.documents = dto.tender.documents!!.asSequence()
                    .filter { docIds.contains(it.id) }.toList()
        }
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

    private fun updateAward(record: Record, award: Award) {
        if (record.awards != null) {
            val updatableAward = record.awards!!.asSequence().firstOrNull { it.id == award.id }
                    ?: throw ErrorException(ErrorType.AWARD_NOT_FOUND)
            award.date?.let { updatableAward.date = award.date }
            award.description?.let { updatableAward.description = award.description }
            award.statusDetails?.let { updatableAward.statusDetails = award.statusDetails }
            award.documents?.isNotEmpty().let { updatableAward.documents = award.documents }
            award.statusDetails?.let { updatableAward.statusDetails = award.statusDetails }
        }
    }

    private fun updateBid(record: Record, bid: Bid) {
        if (record.bids?.details != null) {
            val updatableBid = record.bids!!.details!!.asSequence().firstOrNull { it.id == bid.id }
                    ?: throw ErrorException(ErrorType.BID_NOT_FOUND)
            bid.date?.let { updatableBid.date = bid.date }
            bid.statusDetails?.let { updatableBid.statusDetails = bid.statusDetails }
        }
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }

}
