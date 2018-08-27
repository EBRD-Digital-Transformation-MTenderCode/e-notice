package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.*
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.model.tender.record.RecordTender
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface TenderService {

    fun tenderPeriodEnd(cpid: String,
                        ocid: String,
                        stage: String,
                        releaseDate: LocalDateTime,
                        data: JsonNode): ResponseDto

    fun suspendTender(cpid: String,
                      ocid: String,
                      stage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto

    fun tenderUnsuccessful(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto

    fun awardByBid(cpid: String,
                   ocid: String,
                   stage: String,
                   releaseDate: LocalDateTime,
                   data: JsonNode): ResponseDto

    fun awardPeriodEnd(cpid: String,
                       ocid: String,
                       stage: String,
                       releaseDate: LocalDateTime,
                       data: JsonNode): ResponseDto

    fun standstillPeriod(cpid: String,
                         ocid: String,
                         stage: String,
                         releaseDate: LocalDateTime,
                         data: JsonNode): ResponseDto

    fun startNewStage(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto
}

@Service
class TenderServiceImpl(private val releaseService: ReleaseService,
                        private val organizationService: OrganizationService,
                        private val relatedProcessService: RelatedProcessService) : TenderService {

    override fun tenderPeriodEnd(cpid: String,
                                 ocid: String,
                                 stage: String,
                                 releaseDate: LocalDateTime,
                                 data: JsonNode): ResponseDto {
        val dto = toObject(TenderPeriodEndDto::class.java, data.toString())
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.AWARD)
            tender.awardPeriod = dto.awardPeriod
            if (dto.awards.isNotEmpty()) awards = dto.awards
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.bids.isNotEmpty() && dto.documents.isNotEmpty()) updateBidsDocuments(dto.bids, dto.documents)
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    override fun suspendTender(cpid: String,
                               ocid: String,
                               stage: String,
                               releaseDate: LocalDateTime,
                               data: JsonNode): ResponseDto {
        val dto = toObject(SuspendTenderDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = dto.tender.statusDetails
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    override fun tenderUnsuccessful(cpid: String,
                                    ocid: String,
                                    stage: String,
                                    releaseDate: LocalDateTime,
                                    data: JsonNode): ResponseDto {
        val dto = toObject(UnsuccessfulTenderDto::class.java, data.toString())
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.status = TenderStatus.UNSUCCESSFUL
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.UNSUCCESSFUL
            tender.statusDetails = TenderStatusDetails.EMPTY
            if (dto.lots != null) tender.lots?.let { updateLots(it, dto.lots) }
            if (dto.bids != null) bids?.details?.let { updateBids(it, dto.bids) }
            if (dto.awards != null) awards?.let { updateAwards(it, dto.awards) }
        }
        releaseService.saveMs(cpid, ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    override fun awardByBid(cpid: String,
                            ocid: String,
                            stage: String,
                            releaseDate: LocalDateTime,
                            data: JsonNode): ResponseDto {
        val dto = toObject(AwardByBidDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    override fun awardPeriodEnd(cpid: String,
                                ocid: String,
                                stage: String,
                                releaseDate: LocalDateTime,
                                data: JsonNode): ResponseDto {
        val dto = toObject(AwardPeriodEndDto::class.java, data.toString())
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.awards.isNotEmpty()) awards?.let { updateAwards(it, dto.awards) }
            if (dto.bids.isNotEmpty()) bids?.details?.let { updateBids(it, dto.bids) }
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    override fun standstillPeriod(cpid: String,
                                  ocid: String,
                                  stage: String,
                                  releaseDate: LocalDateTime,
                                  data: JsonNode): ResponseDto {
        val dto = toObject(StandstillPeriodEndDto::class.java, toJson(data))
        val statusDetails = when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PS -> TenderStatusDetails.PRESELECTED
            Stage.PQ -> TenderStatusDetails.PREQUALIFIED
            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = statusDetails
            tender.standstillPeriod = dto.standstillPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
        }
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    override fun startNewStage(cpid: String,
                               ocid: String,
                               stage: String,
                               prevStage: String,
                               releaseDate: LocalDateTime,
                               data: JsonNode): ResponseDto {
        val dto = toObject(StartNewStageDto::class.java, toJson(data))
        val statusDetails: TenderStatusDetails?
        val relatedProcessType: RelatedProcessType?
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
        val prRelatedProcessType = when (Stage.valueOf(prevStage.toUpperCase())) {
            Stage.PS -> RelatedProcessType.X_PRESELECTION
            Stage.PQ -> RelatedProcessType.X_PREQUALIFICATION
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        /*prev record*/
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val prevRecord = releaseService.getRecord(recordEntity.jsonData)
        prevRecord.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        /*new record*/
        val newOcId = releaseService.newOcId(cpId = cpid, stage = stage)
        dto.tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
        dto.tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
        val record = Record(
                ocid = newOcId,
                id = releaseService.newReleaseId(newOcId),
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
        processTenderDocuments(record = record, prevRecord = prevRecord)
        organizationService.processRecordPartiesFromBids(record)
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record = record, cpId = cpid)
        relatedProcessService.addRecordRelatedProcessToRecord(record = record, ocId = ocid, cpId = cpid, processType = prRelatedProcessType)
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = prevRecord)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun processTenderDocuments(record: Record, prevRecord: Record) {
        prevRecord.tender.documents?.let { updateTenderDocuments(record.tender, it) }
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
                lot.status = this.status
                lot.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateTenderDocuments(tender: RecordTender, documents: HashSet<Document>) {
        tender.documents?.let { tenderDocuments ->
            documents.forEach { document ->
                tenderDocuments.firstOrNull { it.id == document.id }?.apply {
                    datePublished = document.datePublished
                    url = document.url
                }
            }
        }
    }

    private fun updateBidsDocuments(bids: HashSet<Bid>, documents: HashSet<Document>) {
        bids.forEach { bid ->
            bid.documents?.let { bidDocuments ->
                documents.forEach { document ->
                    bidDocuments.firstOrNull { it.id == document.id }?.apply {
                        datePublished = document.datePublished
                        url = document.url
                    }
                }
            }
        }
    }

    private fun updateAward(record: Record, award: Award) {
        record.awards?.let { awards ->
            val upAward = awards.asSequence().firstOrNull { it.id == award.id }
                    ?: throw ErrorException(ErrorType.AWARD_NOT_FOUND)
            award.date?.let { upAward.date = it }
            award.description?.let { upAward.description = it }
            award.statusDetails?.let { upAward.statusDetails = it }
            award.documents?.let { upAward.documents = it }
        }
    }

    private fun updateBid(record: Record, bid: Bid) {
        record.bids?.details?.let { bids ->
            val upBid = bids.asSequence().firstOrNull { it.id == bid.id }
                    ?: throw ErrorException(ErrorType.BID_NOT_FOUND)
            bid.date?.let { upBid.date = it }
            bid.statusDetails?.let { upBid.statusDetails = it }
        }
    }
}