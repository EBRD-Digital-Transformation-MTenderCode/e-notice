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
import kotlin.collections.HashSet

@Service
interface ReleaseService {

    fun getReleaseEntity(cpId: String, stage: String, record: Record): ReleaseEntity

    fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun standstillPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

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

    override fun createCnPnPin(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val checkFs = toObject(CheckFsDto::class.java, data.toString())
        val ms = toObject(Ms::class.java, data.toString())
        with(ms) {
            ocid = cpid
            date = releaseDate
            id = getReleaseId(cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            organizationService.processMsParties(ms, checkFs)
        }
        val record = toObject(Record::class.java, data.toString())
        with(record) {
            date = releaseDate
            ocid = getOcId(cpid, stage)
            id = getReleaseId(record.ocid!!)
            tag = listOf(Tag.TENDER)
            initiationType = InitiationType.TENDER
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            tender.hasEnquiries = false
            hasPreviousNotice = false
            purposeOfNotice = PurposeOfNotice(isACallForCompetition = true)
        }
        when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PS -> {
                ms.tender.statusDetails = TenderStatusDetails.PRESELECTION
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.X_PRESELECTION)
            }
            Stage.PQ -> {
                ms.tender.statusDetails = TenderStatusDetails.PREQUALIFICATION
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.X_PREQUALIFICATION)
            }
            Stage.PN -> {
                ms.tender.statusDetails = TenderStatusDetails.PLANNING_NOTICE
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.PLANNING)
            }
            Stage.PIN -> {
                ms.tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.PRIOR)
            }
            Stage.EV -> {
                throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
            }
            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.ocid!!)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        budgetService.createEiByMs(checkFs.ei, cpid, releaseDate)
        budgetService.createFsByMs(ms.planning!!.budget!!.budgetBreakdown!!, cpid, releaseDate)
        return getResponseDto(ms.ocid!!, record.ocid!!)
    }

    override fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(TenderPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(record.ocid!!)
            date = releaseDate
            tag = listOf(Tag.AWARD)
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = HashSet(dto.awards)
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
    }

    override fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(SuspendTenderDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(record.ocid!!)
            date = releaseDate
            tender.statusDetails = dto.tender.statusDetails
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
    }

    override fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardByBidDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(record.ocid!!)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
    }

    override fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(record.ocid!!)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = HashSet(dto.awards)
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
    }

    override fun standstillPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val dto = toObject(StandstillPeriodEndDto::class.java, toJson(data))
        /*MS*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        with(ms) {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = TenderStatusDetails.PRESELECTED
        }
        releaseDao.saveRelease(getMSEntity(ms.ocid!!, ms))
        /*Record*/
        val releaseEntity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, releaseEntity.jsonData)
        with(record) {
            id = getReleaseId(record.ocid!!)
            date = dto.standstillPeriod.endDate
            tender.statusDetails = TenderStatusDetails.PRESELECTED
            tender.standstillPeriod = dto.standstillPeriod
            tender.lots = dto.lots
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
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
        with(prevRecord) {
            id = getReleaseId(ocid!!)
            date = releaseDate
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getReleaseEntity(cpid, prevStage, prevRecord))
        }
        /*new record*/
        val ocid = getOcId(cpid, stage)
        val record = Record(
                ocid = ocid,
                id = getReleaseId(ocid),
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
        relatedProcessService.addRecordRelatedProcessToMs(ms, record.ocid!!, relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.ocid!!)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prevRecord.ocid!!, ms.ocid!!)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
    }

    override fun createPinOnPn(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val msTender = toObject(MsTender::class.java, toJson(data.get(TENDER_JSON)))
        val recordTender = toObject(RecordTender::class.java, toJson(data.get(TENDER_JSON)))
        /*Multi stage*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        val prevProcuringEntity = ms.tender.procuringEntity
        with(ms) {
            id = getReleaseId(ms.ocid!!)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender = msTender
            tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
            tender.procuringEntity = prevProcuringEntity
        }
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prevRecordOcId = record.ocid!!
        with(record) {
            /* previous record*/
            id = getReleaseId(record.ocid!!)
            date = releaseDate
            tag = listOf(Tag.PLANNING_UPDATE)
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
            /*new record*/
            ocid = getOcId(cpid, stage)
            id = getReleaseId(record.ocid!!)
            date = releaseDate
            tag = listOf(Tag.PLANNING)
            initiationType = InitiationType.TENDER
            tender = recordTender
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            hasPreviousNotice = true
            purposeOfNotice?.isACallForCompetition = false
        }
        relatedProcessService.addRecordRelatedProcessToMs(ms, record.ocid!!, RelatedProcessType.PRIOR)
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.ocid!!)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prevRecordOcId, ms.ocid!!)
        releaseDao.saveRelease(getMSEntity(cpid, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid, record.ocid!!)
    }

    private fun processDocuments(record: Record, dto: StartNewStageDto) {
        if (dto.tender.documents != null) {
            val docIds = dto.tender.documents!!.map { it.id!! }.toSet()
            record.tender.documents = dto.tender.documents!!.asSequence()
                    .filter { docIds.contains(it.id) }.toList()
        }
    }

    private fun getMSEntity(cpId: String, ms: Ms): ReleaseEntity {
        return getEntity(
                cpId = cpId,
                ocId = ms.ocid!!,
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

    private fun updateLots(record: Record, lotsDto: List<Lot>) {
        val lots = record.tender.lots
        if (lots == null || lots.isEmpty()) throw ErrorException(ErrorType.LOT_NOT_FOUND)
        val updatableLots = lots.asSequence().map { it.id!! to it }.toMap()
        lotsDto.asSequence().forEach { updatableLots[it.id]?.statusDetails = it.statusDetails }
        record.tender.lots = updatableLots.values.toList()
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }

}
