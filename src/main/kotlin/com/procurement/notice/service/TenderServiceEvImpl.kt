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
interface TenderServiceEv {

    fun tenderPeriodEndEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardByBidEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun awardPeriodEndEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun standstillPeriodEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

}

@Service
class TenderServiceEvImpl(private val releaseDao: ReleaseDao,
                        private val releaseService: ReleaseService,
                        private val organizationService: OrganizationService,
                        private val relatedProcessService: RelatedProcessService) : TenderServiceEv {

    companion object {
        private val SEPARATOR = "-"
        private val MS = "MS"
    }

    override fun tenderPeriodEndEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode):
            ResponseDto<*> {
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

    override fun standstillPeriodEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode):
            ResponseDto<*> {
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

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
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

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }
}