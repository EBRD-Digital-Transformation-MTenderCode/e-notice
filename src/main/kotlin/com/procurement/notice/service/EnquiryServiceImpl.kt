package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.tender.dto.UnsuspendTenderDto
import com.procurement.notice.model.tender.enquiry.RecordEnquiry
import com.procurement.notice.model.tender.ms.Ms
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
                         private val organizationService: OrganizationService,
                         private val releaseDao: ReleaseDao) : EnquiryService {

    companion object {
        private val SEPARATOR = "-"
        private val ENQUIRY_JSON = "enquiry"
        private val MS = "MS"
    }

    override fun createEnquiry(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val enquiry = toObject(RecordEnquiry::class.java, toJson(data.get(ENQUIRY_JSON)))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        /*record*/
        record.id = getReleaseId(ocId)
        record.date = releaseDate
        record.tender.hasEnquiries = true
        /*add enquiry*/
        var enquiries = record.tender.enquiries ?: hashSetOf()
        if (enquiries.asSequence().none { it.id == enquiry.id }) {
            enquiries.add(enquiry)
            organizationService.processRecordPartiesFromEnquiry(record, enquiry)
            record.tender.enquiries = enquiries
            /*ms*/
            if (enquiries.size == 1) {
                val msEntity = releaseDao.getByCpIdAndStage(cpid, MS) ?: throw ErrorException(ErrorType.MS_NOT_FOUND)
                val ms = toObject(Ms::class.java, msEntity.jsonData).apply {
                    id = getReleaseId(cpid)
                    date = releaseDate
                    tag = listOf(Tag.COMPILED)
                    tender.hasEnquiries = true
                }
                releaseDao.saveRelease(releaseService.getMSEntity(cpid, ms))
            }
        }
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun addAnswer(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage) ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val enquiry = toObject(RecordEnquiry::class.java, toJson(data.get(ENQUIRY_JSON)))
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        record.id = getReleaseId(ocId)
        record.date = releaseDate
        record.tender.enquiries?.asSequence()?.firstOrNull { it.id == enquiry.id }?.apply { this.answer = enquiry.answer }
                ?: throw ErrorException(ErrorType.ENQUIRY_NOT_FOUND)
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    override fun unsuspendTender(cpid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto<*> {
        val entity = releaseDao.getByCpIdAndStage(cpid, stage)
                ?: throw  ErrorException(ErrorType.DATA_NOT_FOUND)
        val record = toObject(Record::class.java, entity.jsonData)
        val ocId = record.ocid ?: throw ErrorException(ErrorType.OCID_ERROR)
        val dto = toObject(UnsuspendTenderDto::class.java, toJson(data))
        record.date = releaseDate
        record.id = getReleaseId(ocId)
        record.tender.statusDetails = dto.tender.statusDetails
        record.tender.tenderPeriod = dto.tender.tenderPeriod
        record.tender.enquiryPeriod = dto.tender.enquiryPeriod
        addAnswerToEnquiry(record.tender.enquiries, dto.enquiry)
        releaseDao.saveRelease(releaseService.getRecordEntity(cpid, stage, record))
        return getResponseDto(cpid, ocId)
    }

    private fun addAnswerToEnquiry(enquiries: HashSet<RecordEnquiry>?, enquiry: RecordEnquiry) {
        enquiries?.asSequence()?.firstOrNull { it.id == enquiry.id }?.apply {
            this.answer = enquiry.answer
        } ?: throw ErrorException(ErrorType.ENQUIRY_NOT_FOUND)
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