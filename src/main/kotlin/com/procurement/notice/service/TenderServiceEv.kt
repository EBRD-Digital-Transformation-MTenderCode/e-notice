package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.AwardByBidEvDto
import com.procurement.notice.model.tender.dto.AwardPeriodEndEvDto
import com.procurement.notice.model.tender.dto.StandstillPeriodEndEvDto
import com.procurement.notice.model.tender.dto.TenderStatusDto
import com.procurement.notice.model.tender.record.ContractRecord
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

@Service
class TenderServiceEv(private val releaseService: ReleaseService,
                      private val organizationService: OrganizationService,
                      private val relatedProcessService: RelatedProcessService) {

    companion object {
        private const val AC = "AC"
    }

    fun awardByBidEv(cpid: String,
                     ocid: String,
                     stage: String,
                     releaseDate: LocalDateTime,
                     data: JsonNode): ResponseDto {
        val dto = toObject(AwardByBidEvDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
            dto.lot?.let { lot -> updateLot(this, lot) }
            dto.nextAwardForUpdate?.let { award -> updateAward(this, award) }
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun standstillPeriodEv(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto {
        val dto = toObject(StandstillPeriodEndEvDto::class.java, toJson(data))
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = TenderStatusDetails.EVALUATED
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.AWARD_UPDATE)
            tender.standstillPeriod = dto.standstillPeriod
            if (dto.cans.isNotEmpty()) contracts = dto.cans.asSequence().map { it -> it.contract }.toHashSet()
        }
        releaseService.saveMs(cpid, ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun awardPeriodEndEv(cpid: String,
                         ocid: String,
                         stage: String,
                         releaseDate: LocalDateTime,
                         data: JsonNode): ResponseDto {
        val dto = toObject(AwardPeriodEndEvDto::class.java, data.toString())
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.EXECUTION
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.AWARD_UPDATE)
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.awards.isNotEmpty()) awards?.let { updateAwards(it, dto.awards) }
            if (dto.bids.isNotEmpty()) bids?.details?.let { updateBids(it, dto.bids) }
            if (dto.cans.isNotEmpty()) {
                val contractsDto = dto.cans.asSequence().map { it -> it.contract }.toHashSet()
                contracts?.let { updateContracts(it, contractsDto) }
            }
        }
        val contractRecords = mutableListOf<ContractRecord>()
        if (dto.contracts.isNotEmpty()) {
            for (contract in dto.contracts) {
                val ocIdContract = contract.id!!
                val award = dto.awards.asSequence().first { it.id == contract.awardId }
                val recordContract = ContractRecord(
                        ocid = ocIdContract,
                        id = releaseService.newReleaseId(ocIdContract),
                        date = releaseDate,
                        tag = listOf(Tag.CONTRACT),
                        initiationType = record.initiationType,
                        parties = null,
                        awards = setOf(award).toHashSet(),
                        contracts = setOf(contract).toHashSet(),
                        relatedProcesses = null)
                organizationService.processContractRecordPartiesFromAwards(recordContract)
                relatedProcessService.addMsRelatedProcessToContract(record = recordContract, cpId = cpid)
                relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = ocIdContract, processType = RelatedProcessType.X_CONTRACTING)
                relatedProcessService.addRecordRelatedProcessToContractRecord(record = recordContract, ocId = ocid, cpId = cpid, processType = RelatedProcessType.X_EVALUATION)
                relatedProcessService.addContractRelatedProcessToCAN(record = record, ocId = ocIdContract, cpId = cpid, contract = contract)
                contractRecords.add(recordContract)
            }
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        contractRecords.forEach { recordContract ->
            releaseService.saveContractRecord(cpId = cpid, stage = AC, record = recordContract, publishDate = releaseDate.toDate())
        }
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun enquiryPeriodEnd(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(TenderStatusDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = dto.tenderStatusDetails
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
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

    private fun updateBidsDocuments(bids: HashSet<Bid>, documents: HashSet<Document>) {
        for (bid in bids) if (bid.documents != null) for (document in documents)
            bid.documents?.firstOrNull { it.id == document.id }?.apply {
                datePublished = document.datePublished
                url = document.url
            }
    }

}