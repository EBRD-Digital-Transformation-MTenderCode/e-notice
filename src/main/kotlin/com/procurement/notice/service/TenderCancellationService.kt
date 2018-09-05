package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.CancellationStandstillPeriodDto
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.model.tender.record.RecordTender
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.util.*

interface TenderCancellationService {

    fun cancellationStandstillPeriod(cpid: String,
                                     ocid: String,
                                     stage: String,
                                     releaseDate: LocalDateTime,
                                     data: JsonNode): ResponseDto

    fun tenderCancellation(cpid: String,
                           ocid: String,
                           stage: String,
                           releaseDate: LocalDateTime,
                           data: JsonNode): ResponseDto
}

@Service
class TenderCancellationServiceImpl(private val releaseService: ReleaseService) : TenderCancellationService {

    override fun cancellationStandstillPeriod(cpid: String,
                                              ocid: String,
                                              stage: String,
                                              releaseDate: LocalDateTime,
                                              data: JsonNode): ResponseDto {
        val dto = toObject(CancellationStandstillPeriodDto::class.java, toJson(data))
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.statusDetails = TenderStatusDetails.STANDSTILL
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val actualReleaseID = record.id
        val newReleaseID = releaseService.newReleaseId(ocid)
        var amendments = record.tender.amendments?.toMutableList() ?: mutableListOf()
        val relatedLots = dto.lots?.map { it.id }?.toSet()
        if (dto.amendments!= null && dto.amendments.isNotEmpty()){
        amendments.add(Amendment(
                id = UUID.randomUUID().toString(),
                amendsReleaseID = actualReleaseID,
                releaseID = newReleaseID,
                date = releaseDate,
                relatedLots = relatedLots,
                rationale = dto.amendments[0].rationale,
                changes = null,
                description = dto.amendments[0].description
        ))}
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.statusDetails = TenderStatusDetails.CANCELLED
            tender.standstillPeriod = dto.standstillPeriod
            tender.amendments = amendments
            if (dto.lots != null) tender.lots?.let { updateLots(it, dto.lots) }
            if (dto.bids != null) bids?.details?.let { updateBids(it, dto.bids) }
            if (dto.awards != null) awards?.let { updateAwards(it, dto.awards) }
        }
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        val amendmentsIds = amendments.asSequence().map { it.id!! }.toSet()
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid, amendmentsIds = amendmentsIds))
    }

    override fun tenderCancellation(cpid: String,
                                    ocid: String,
                                    stage: String,
                                    releaseDate: LocalDateTime,
                                    data: JsonNode): ResponseDto {
        val dto = toObject(CancellationStandstillPeriodDto::class.java, toJson(data))
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.CANCELLED
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER_CANCELLATION)
            tender.status = TenderStatus.CANCELLED
            tender.statusDetails = TenderStatusDetails.EMPTY
//            tender.standstillPeriod = dto.standstillPeriod
            if (dto.lots != null) tender.lots?.let { updateLots(it, dto.lots) }
            if (dto.bids != null) bids?.details?.let { updateBids(it, dto.bids) }
            if (dto.awards != null) awards?.let { updateAwards(it, dto.awards) }
        }
        releaseService.saveMs(cpId = cpid, ms = ms)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
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
}