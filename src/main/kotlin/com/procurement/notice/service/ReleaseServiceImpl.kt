package com.procurement.notice.service

import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.bpe.ResponseDto
import com.procurement.notice.model.entity.ReleaseEntity
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.utils.*
import org.springframework.stereotype.Service

interface ReleaseService {

    fun getReleaseId(ocId: String): String

    fun getOcId(cpId: String, stage: String): String

    fun getRecordEntity(cpId: String, stage: String, record: Record): ReleaseEntity

    fun getMSEntity(cpId: String, ms: Ms): ReleaseEntity

    fun getEntity(cpId: String,
                  ocId: String,
                  releaseId: String,
                  stage: String,
                  json: String,
                  status: String): ReleaseEntity

    fun getResponseDto(cpid: String, ocid: String): ResponseDto
}


@Service
class ReleaseServiceImpl : ReleaseService {

    companion object {
        private const val SEPARATOR = "-"
        private const val TENDER_JSON = "tender"
        private const val MS = "MS"
    }

    override fun getRecordEntity(cpId: String, stage: String, record: Record): ReleaseEntity {
        val ocId = record.ocid ?: throw ErrorException(ErrorType.PARAM_ERROR)
        val releaseId = record.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return getEntity(
                cpId = cpId,
                ocId = ocId,
                releaseId = releaseId,
                stage = stage,
                json = toJson(record),
                status = record.tender.status.toString()
        )
    }

    override fun getMSEntity(cpId: String, ms: Ms): ReleaseEntity {
        val releaseId = ms.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
        return getEntity(
                cpId = cpId,
                ocId = cpId,
                releaseId = releaseId,
                stage = MS,
                json = toJson(ms),
                status = ms.tender.status.toString()
        )
    }

    override fun getEntity(cpId: String,
                          ocId: String,
                          releaseId: String,
                          stage: String,
                          json: String,
                          status: String): ReleaseEntity {
        return ReleaseEntity(
                cpId = cpId,
                ocId = ocId,
                releaseDate = localNowUTC().toDate(),
                releaseId = releaseId,
                stage = stage,
                jsonData = json,
                status = status
        )
    }

    override fun getOcId(cpId: String, stage: String): String {
        return cpId + SEPARATOR + stage.toUpperCase() + SEPARATOR + milliNowUTC()
    }

    override fun getReleaseId(ocId: String): String {
        return ocId + SEPARATOR + milliNowUTC()
    }

    override fun getResponseDto(cpid: String, ocid: String): ResponseDto {
        val jsonForResponse = createObjectNode()
        jsonForResponse.put("cpid", cpid)
        jsonForResponse.put("ocid", ocid)
        return ResponseDto(true, null, jsonForResponse)
    }

}
