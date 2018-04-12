package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.tender.dto.UnsuspendTenderDto
import com.procurement.notice.model.tender.enquiry.RecordEnquiry
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.createObjectNode
import com.procurement.notice.utils.milliNowUTC
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
interface EnquiryService {

    fun createEnquiry(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun addAnswer(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>

    fun unsuspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*>
}

@Service
class EnquiryServiceImpl(private val releaseService: ReleaseService,
                         private val releaseDao: ReleaseDao) : EnquiryService {

    companion object {
        private val SEPARATOR = "-"
        private val ENQUIRY_JSON = "enquiry"
    }

    override fun createEnquiry(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val enquiry = toObject(RecordEnquiry::class.java, toJson(data.get(ENQUIRY_JSON)))
        val release = toObject(Record::class.java, entity.jsonData)
        addEnquiryToTender(release, enquiry)
        release.id = getReleaseId(release.ocid!!)
        release.date = releaseDate
        releaseDao.saveRelease(releaseService.getReleaseEntity(cpid, stage, release))
        return getResponseDto(cpid, release.ocid!!)
    }

    override fun addAnswer(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val enquiry = toObject(RecordEnquiry::class.java, toJson(data.get(ENQUIRY_JSON)))
        val release = toObject(Record::class.java, entity.jsonData)
        addAnswerToEnquiry(release, enquiry)
        release.id = getReleaseId(release.ocid!!)
        release.date = releaseDate
        releaseDao.saveRelease(releaseService.getReleaseEntity(cpid, stage, release))
        return getResponseDto(cpid, release.ocid!!)
    }

    override fun unsuspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val release = toObject(Record::class.java, entity.jsonData)
        val dto = toObject(UnsuspendTenderDto::class.java, toJson(data))
        addAnswerToEnquiry(release, dto.enquiry)
        release.date = releaseDate
        release.id = getReleaseId(release.ocid!!)
        release.tender.statusDetails = dto.tender.statusDetails
        release.tender.tenderPeriod = dto.tenderPeriod
        release.tender.enquiryPeriod = dto.enquiryPeriod
        releaseDao.saveRelease(releaseService.getReleaseEntity(cpid, stage, release))
        return getResponseDto(cpid, release.ocid!!)
    }

    private fun addEnquiryToTender(release: Record, enquiry: RecordEnquiry) {
        if (release.tender.enquiries != null) {
            val index = release.tender.enquiries!!.indexOfFirst { it.id == enquiry.id }
            if (index != -1) release.tender.enquiries!![index] = enquiry
            else release.tender.enquiries!!.add(enquiry)
        }
    }

    private fun addAnswerToEnquiry(release: Record, enquiry: RecordEnquiry) {
        if (release.tender.enquiries != null) {
            val index = release.tender.enquiries!!.indexOfFirst { it.id == enquiry.id }
            if (index != -1) release.tender.enquiries!![index].answer = enquiry.answer
            else throw ErrorException(ErrorType.DATA_NOT_FOUND)
        }
    }

    private fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    private fun getResponseDto(cpid: String, ocid: String): ResponseDto<*> {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }
}