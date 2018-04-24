package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.*
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.createObjectNode
import com.procurement.notice.utils.milliNowUTC
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
interface TenderService {

    fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardByBidEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardPeriodEndEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun standstillPeriod(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun startNewStage(cpid: String, stage: String, prevStage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>
}

@Service
class TenderServiceImpl(private val releaseDao: ReleaseDao,
                        private val releaseService: ReleaseService,
                        private val organizationService: OrganizationService,
                        private val relatedProcessService: RelatedProcessService) : TenderService {

    companion object {
        private val SEPARATOR = "-"
        private val MS = "MS"
    }

    override fun tenderPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(TenderPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            date = releaseDate
            tag = listOf(Tag.AWARD)
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = dto.awards
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty() && dto.documents.isNotEmpty()) updateBidsDocuments(dto.bids, dto.documents)
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)

        }
        if (Stage.valueOf(stage.toUpperCase()) == Stage.PS) organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun suspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(SuspendTenderDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = dto.tender.statusDetails
        }
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun awardByBid(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardByBidDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
        }
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun awardByBidEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardByBidEvDto::class.java, toJson(data))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            if (dto.awards.isNotEmpty()) updateAwards(awards!!, dto.awards)
            if (dto.bids.isNotEmpty()) updateBids(bids?.details!!, dto.bids)
            if (dto.lots != null && dto.lots.isNotEmpty()) {
                tender.lots?.let { updateLots(it, dto.lots) }
            }
        }
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun awardPeriodEnd(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardPeriodEndDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.awards.isNotEmpty()) updateAwards(awards!!, dto.awards)
            if (dto.bids.isNotEmpty()) updateBids(bids?.details!!, dto.bids)
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun awardPeriodEndEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = TenderStatusDetails.EXECUTION
        }
        releaseDao.saveRelease(releaseService.getMSEntity(cpid, ms))

        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardPeriodEndEvDto::class.java, data.toString())
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.awards.isNotEmpty()) updateAwards(awards!!, dto.awards)
            if (dto.bids.isNotEmpty()) updateBids(bids?.details!!, dto.bids)
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
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
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        releaseDao.saveRelease(releaseService.getMSEntity(cpid, ms))
        /*record*/
        val releaseEntity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val record = toObject(Record::class.java, releaseEntity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.apply {
            id = getReleaseId(ocId)
            date = releaseDate
            tender.statusDetails = statusDetails
            tender.standstillPeriod = dto.standstillPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
        }
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
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
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS)
                ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        ms.apply {
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
        prevRecord.apply {
            id = getReleaseId(prOcId)
            date = releaseDate
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
            releaseDao.saveRelease(releaseService.getRecordEntity(cpid, prevStage, prevRecord))
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
                contracts = null,
                hasPreviousNotice = prevRecord.hasPreviousNotice,
                purposeOfNotice = prevRecord.purposeOfNotice,
                relatedProcesses = null)
        processDocuments(record, dto)
        organizationService.processRecordPartiesFromBids(record)
        relatedProcessService.addRecordRelatedProcessToMs(ms, ocId, relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record, cpid)
        relatedProcessService.addPervRecordRelatedProcessToRecord(record, prOcId, cpid, RelatedProcessType.X_PRESELECTION)
        releaseDao.saveRelease(releaseService.getMSEntity(cpid, ms))
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    private fun getOcId(cpId: String, stage: String): String {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun processDocuments(record: Record, dto: StartNewStageDto) {
        if (dto.tender.documents != null) {
            val docIds = dto.tender.documents!!.asSequence().map { it.id!! }.toSet()
            record.tender.documents = dto.tender.documents!!.asSequence()
                    .filter { docIds.contains(it.id) }.toList()
        }
    }

    private fun updateAwards(recordAwards: HashSet<Award>, dtoAwards: HashSet<Award>) {
        for (award in recordAwards) {
            dtoAwards.firstOrNull { it.id == award.id }?.apply {
                award.date = this.date
                award.status = this.status
                award.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateBids(recordBids: HashSet<Bid>, dtoBids: HashSet<Bid>) {
        for (bid in recordBids) {
            dtoBids.firstOrNull { it.id == bid.id }?.apply {
                bid.date = this.date
                bid.status = this.status
                bid.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateLots(recordLots: HashSet<Lot>, dtoLots: HashSet<Lot>) {
        for (lot in recordLots) {
            dtoLots.firstOrNull { it.id == lot.id }?.apply {
                recordLots.minus(lot)
                recordLots.plus(this)
            }
        }
    }

    private fun updateBidsDocuments(bids: HashSet<Bid>, documents: HashSet<Document>) {
        for (bid in bids) if (bid.documents != null) for (document in documents)
            bid.documents!!.firstOrNull { it.id == document.id }?.apply {
                datePublished = document.datePublished
                url = document.url
            }
    }

    private fun updateAward(record: Record, award: Award) {
        if (record.awards != null) {
            val updatableAward = record.awards!!.asSequence().firstOrNull { it.id == award.id }
                    ?: throw ErrorException(ErrorType.AWARD_NOT_FOUND)
            if (award.date != null) updatableAward.date = award.date
            if (award.description != null) updatableAward.description = award.description
            if (award.statusDetails != null) updatableAward.statusDetails = award.statusDetails
            if (award.documents != null) updatableAward.documents = award.documents
        }
    }

    private fun updateBid(record: Record, bid: Bid) {
        if (record.bids?.details != null) {
            val updatableBid = record.bids!!.details!!.asSequence().firstOrNull { it.id == bid.id }
                    ?: throw ErrorException(ErrorType.BID_NOT_FOUND)
            if (bid.date != null) updatableBid.date = bid.date
            if (bid.statusDetails != null) updatableBid.statusDetails = bid.statusDetails
        }
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }
}