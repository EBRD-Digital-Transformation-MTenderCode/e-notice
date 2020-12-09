package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.dto.AwardByBidEvDto
import com.procurement.notice.model.tender.dto.TenderStatusDto
import com.procurement.notice.model.tender.dto.UpdateBidDocsDto
import com.procurement.notice.model.tender.record.Release
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TenderServiceEv(private val releaseService: ReleaseService,
                      private val organizationService: OrganizationService,
                      private val relatedProcessService: RelatedProcessService,
                      private val generationService: GenerationService
) {

    fun awardByBidEv(cpid: String,
                     ocid: String,
                     stage: String,
                     releaseDate: LocalDateTime,
                     data: JsonNode): ResponseDto {
        val dto = toObject(AwardByBidEvDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            dto.bid?.let { bid -> updateBid(this, bid) }
            dto.lot?.let { lot -> updateLot(this, lot) }
            dto.nextAwardForUpdate?.let { nextAward -> updateNextAward(this, nextAward) }
            dto.consideredBid?.let { consideredBid -> updateBidDocuments(this, consideredBid) }
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun updateBidDocs(cpid: String,
                      ocid: String,
                      stage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val dto = toObject(UpdateBidDocsDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
        }
        release.bids?.details?.let { bids ->
            bids.asSequence()
                    .firstOrNull { it.id == dto.bid.id }
                    ?.let { bid ->
                        bid.documents = dto.bid.documents
                    }
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

      fun enquiryPeriodEnd(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(TenderStatusDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)
        release.apply {
            id = generationService.generateReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.TENDER)
            tender.statusDetails = dto.tenderStatusDetails
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, release = release, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun updateAward(release: Release, award: Award) {
        release.awards
            .let { awards ->
                val upAward = awards.asSequence().firstOrNull { it.id == award.id }
                    ?: throw ErrorException(ErrorType.AWARD_NOT_FOUND)
                award.date?.let { upAward.date = it }
                award.description?.let { upAward.description = it }
                award.statusDetails?.let { upAward.statusDetails = it }
                award.documents?.let { upAward.documents = it }
            }
    }

    private fun updateNextAward(release: Release, nextAward: Award) {
        release.awards
            .let { awards ->
                val upAward = awards.firstOrNull { it.id == nextAward.id }
                    ?: throw ErrorException(ErrorType.AWARD_NOT_FOUND)
                nextAward.statusDetails?.let { upAward.statusDetails = it }
            }
    }

    private fun updateBid(release: Release, bid: Bid) {
        release.bids?.details?.let { bids ->
            val upBid = bids.asSequence().firstOrNull { it.id == bid.id }
                    ?: throw ErrorException(ErrorType.BID_NOT_FOUND)
            bid.date?.let { upBid.date = it }
            bid.statusDetails?.let { upBid.statusDetails = it }
        }
    }

    private fun updateLot(release: Release, lot: Lot) {
        release.tender.lots
            .let { lots ->
                val upLot = lots.asSequence().firstOrNull { it.id == lot.id }
                    ?: throw ErrorException(ErrorType.LOT_NOT_FOUND)
                lot.statusDetails?.let { upLot.statusDetails = it }
            }
    }

    private fun updateBidDocuments(release: Release, bid: Bid) {
        release.bids?.details
            ?.let { bids ->
                val upBid = bids.asSequence().firstOrNull { it.id == bid.id }
                    ?: throw ErrorException(ErrorType.BID_NOT_FOUND)
                upBid.documents = bid.documents
            }
    }

}