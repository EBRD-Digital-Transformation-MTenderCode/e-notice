package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.AwardByBidEvDto
import com.procurement.notice.model.tender.dto.AwardPeriodEndEvDto
import com.procurement.notice.model.tender.dto.StandstillPeriodEndEvDto
import com.procurement.notice.model.tender.dto.TenderPeriodEndDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.ContractRecord
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.*
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

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
        private val CN = "CN"
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
        organizationService.processRecordPartiesFromBids(record)
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
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
            dto.lot?.let { lot -> updateLot(this, lot) }
            dto.nextAward?.let { award -> updateAward(this, award) }
        }
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun standstillPeriodEv(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val dto = toObject(StandstillPeriodEndEvDto::class.java, toJson(data))
        /*ms*/
        val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
        val ms = toObject(Ms::class.java, msEntity.jsonData)
        ms.apply {
            id = getReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = TenderStatusDetails.EVALUATED
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
            tag = listOf(Tag.AWARD_UPDATE)
            tender.standstillPeriod = dto.standstillPeriod
            if (dto.cans.isNotEmpty()) contracts = dto.cans.asSequence().map { it -> it.contract }.toHashSet()
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

        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val dto = toObject(AwardPeriodEndEvDto::class.java, data.toString())
        val recordEv = toObject(Record::class.java, entity.jsonData)
        val recordEvOcId = recordEv.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        val contractsDto = dto.cans.asSequence().map { it -> it.contract }.toHashSet()
        recordEv.apply {
            id = getReleaseId(recordEvOcId)
            date = releaseDate
            tag = listOf(Tag.AWARD_UPDATE)
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.awards.isNotEmpty()) awards?.let { updateAwards(it, dto.awards) }
            if (dto.bids.isNotEmpty()) bids?.details?.let { updateBids(it, dto.bids) }
            if (contractsDto.isNotEmpty()) contracts?.let { updateContracts(it, contractsDto) }
        }
        if (contractsDto.isNotEmpty()) {
            for (contract in contractsDto) {
                /*new record Contract*/
                val ocIdCAN = getOcId(cpid, CN)
                val award = dto.awards.asSequence().first { it.id == contract.awardID }
                val recordCAN = ContractRecord(
                        ocid = ocIdCAN,
                        id = getReleaseId(ocIdCAN),
                        date = releaseDate,
                        tag = listOf(Tag.CONTRACT),
                        initiationType = recordEv.initiationType,
                        parties = null,
                        awards = setOf(award).toHashSet(),
                        contracts = setOf(contract).toHashSet(),
                        relatedProcesses = null)

                relatedProcessService.addMsRelatedProcessToContract(recordCAN, cpid)
                relatedProcessService.addRecordRelatedProcessToMs(ms, ocIdCAN, RelatedProcessType.X_CONTRACT)
                relatedProcessService.addRecordRelatedProcessToContract(recordCAN, recordEvOcId, cpid, RelatedProcessType.X_EVALUATION)
                relatedProcessService.addRecordRelatedProcessToRecord(recordEv, ocIdCAN, cpid, RelatedProcessType.X_CONTRACT)
                releaseDao.saveRelease(getRecordEntity(cpid, CN, recordCAN))
            }
        }
        relatedProcessService.addRecordRelatedProcessToMs(ms, recordEvOcId, RelatedProcessType.X_EVALUATION)
        releaseDao.saveRelease(releaseService.getMSEntity(cpid, ms))
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, recordEv))
        return getResponseDto(cpid, recordEvOcId)
    }

    private fun getOcId(cpId: String, stage: String): String {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun updateContracts(recordContracts: HashSet<Contract>, dtoContracts: HashSet<Contract>) {
        for (contract in recordContracts) {
            dtoContracts.firstOrNull { it.id == contract.id }?.apply {
                contract.status = this.status
                contract.statusDetails = this.statusDetails
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

    private fun updateLot(record: Record, lot: Lot) {
        record.tender.lots?.let { lots ->
            val upLot = lots.asSequence().firstOrNull { it.id == lot.id }
                    ?: throw ErrorException(ErrorType.LOT_NOT_FOUND)
            lot.statusDetails?.let { upLot.statusDetails = it }
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
            bid.documents?.firstOrNull { it.id == document.id }?.apply {
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


    fun getRecordEntity(cpId: String, stage: String, record: ContractRecord): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseDate = record.date ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return getEntity(
                cpId = cpId,
                ocId = ocId,
                releaseDate = releaseDate.toDate(),
                releaseId = releaseId,
                stage = stage,
                json = toJson(record)
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
}