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
import com.procurement.notice.model.tender.record.Record
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
}


@Service
class ReleaseServiceImpl(private val releaseDao: ReleaseDao,
                         private val budgetService: BudgetService,
                         private val organizationService: OrganizationService,
                         private val relatedProcessService: RelatedProcessService) : ReleaseService {

    companion object {
        private val SEPARATOR = "-"
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
            id = getReleaseId(ocId = cpid)
            tag = listOf(Tag.COMPILED)
            initiationType = InitiationType.TENDER
            organizationService.processMsParties(ms = ms, checkFs = checkFs)
        }
        val record = toObject(Record::class.java, data.toString())
        with(record) {
            date = releaseDate
            ocid = getOcId(cpId = cpid, stage = stage)
            id = getReleaseId(ocId = record.ocid!!)
            tag = listOf(Tag.TENDER)
            initiationType = InitiationType.TENDER
            tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
            tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
            purposeOfNotice?.isACallForCompetition = true
        }
        when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PS -> {
                ms.tender.statusDetails = TenderStatusDetails.PRESELECTION
                record.tender.statusDetails = TenderStatusDetails.PRESELECTION
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.X_PRESELECTION)
            }
            Stage.PQ -> {
                ms.tender.statusDetails = TenderStatusDetails.PREQUALIFICATION
                record.tender.statusDetails = TenderStatusDetails.PREQUALIFICATION
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.X_PREQUALIFICATION)
            }
            Stage.PN -> {
                ms.tender.statusDetails = TenderStatusDetails.PLANNING_NOTICE
                record.tender.statusDetails = TenderStatusDetails.PLANNING_NOTICE
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.PLANNING)
            }
            Stage.PIN -> {
                ms.tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
                record.tender.statusDetails = TenderStatusDetails.PRIOR_NOTICE
                relatedProcessService.addEiFsRecordRelatedProcessToMs(ms, checkFs, record.ocid!!, RelatedProcessType.PRIOR)
            }
            Stage.EV -> { throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
            }

            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.ocid!!)
        releaseDao.saveRelease(getMSEntity(cpid, stage, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        budgetService.createEiByMs(checkFs.ei, cpid, releaseDate)
        budgetService.createFsByMs(ms.planning!!.budget!!.budgetBreakdown!!, cpid, releaseDate)
        return getResponseDto(cpid = ms.ocid!!, ocid = record.ocid!!)
    }

    override fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(TenderPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(ocId = record.ocid!!)
            date = releaseDate
            tag = listOf(Tag.AWARD)
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = HashSet(dto.awards)
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid = cpid, ocid = record.ocid!!)
    }

    override fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(SuspendTenderDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(ocId = record.ocid!!)
            date = releaseDate
            tender.statusDetails = dto.tender.statusDetails
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid = cpid, ocid = record.ocid!!)
    }

    override fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardByBidDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(record.ocid!!)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(record = this, award = dto.award)
            updateBid(record = this, bid = dto.bid)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid = cpid, ocid = record.ocid!!)
    }

    override fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        with(record) {
            id = getReleaseId(ocId = record.ocid!!)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = HashSet(dto.awards)
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid = cpid, ocid = record.ocid!!)
    }

    override fun standstillPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val dto = toObject(StandstillPeriodEndDto::class.java, toJson(data))
        /*MS*/
        val msEntity = releaseDao.getByCpIdAndOcId(cpid, cpid) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        with(ms) {
            id = getReleaseId(ocId = cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = TenderStatusDetails.PRESELECTED
        }
        releaseDao.saveRelease(getMSEntity(ms.ocid!!, stage, ms))
        /*Record*/
        val releaseEntity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, releaseEntity.jsonData)
        with(record) {
            id = getReleaseId(ocId = record.ocid!!)
            date = dto.standstillPeriod.endDate
            tender.statusDetails = TenderStatusDetails.PRESELECTED
            tender.standstillPeriod = dto.standstillPeriod
            updateLots(this, dto.lots)
        }
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid = cpid, ocid = record.ocid!!)
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
        val msEntity = releaseDao.getByCpIdAndOcId(cpid, cpid) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        with(ms) {
            id = getReleaseId(ocId = ms.ocid!!)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        val recordEntity = releaseDao.getByCpIdAndStage(cpid, prevStage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, recordEntity.jsonData)
        val prevRecordOcId = record.ocid!!
        with(record) {
            /* previous record*/
            id = getReleaseId(ocId = record.ocid!!)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            releaseDao.saveRelease(getReleaseEntity(cpId = cpid, stage = stage, record = record))
            /*new record*/
            id = getReleaseId(ocId = record.ocid!!)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            ocid = getOcId(cpId = cpid, stage = stage)
            tender = dto.tender
            bids = dto.bids
            tender.statusDetails = statusDetails
            processDocuments(record = this, dto = dto)
            organizationService.processPartiesFromBids(record = this, bids = dto.bids)
        }
        relatedProcessService.addRecordRelatedProcessToMs(record, ms.ocid!!, relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, ms.ocid!!)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prevRecordOcId, ms.ocid!!)
        releaseDao.saveRelease(getMSEntity(cpid, stage, ms))
        releaseDao.saveRelease(getReleaseEntity(cpid, stage, record))
        return getResponseDto(cpid = cpid, ocid = record.ocid!!)
    }

    private fun processDocuments(record: Record, dto: StartNewStageDto) {
        if (dto.tender.documents != null) {
            val docIds = dto.tender.documents!!.map { it.id!! }.toSet()
            record.tender.documents = dto.tender.documents!!.asSequence()
                    .filter { docIds.contains(it.id) }.toList()
        }
    }

    private fun getMSEntity(cpId: String, stage: String, ms: Ms): ReleaseEntity {
        return getEntity(
                cpId = cpId,
                ocId = ms.ocid!!,
                releaseDate = ms.date!!.toDate(),
                releaseId = ms.id!!,
                stage = stage,
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
