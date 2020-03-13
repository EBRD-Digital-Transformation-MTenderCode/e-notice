package com.procurement.notice.service

import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.infrastructure.dto.entity.Record
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.infrastructure.handler.UpdateResult
import com.procurement.notice.infrastructure.service.Transform
import com.procurement.notice.infrastructure.service.record.updateRelease
import com.procurement.notice.utils.toJson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UpdateRecordService(
    private val releaseService: ReleaseService,
    private val jacksonJsonTransform: Transform
) {
    companion object {
        private val log = LoggerFactory.getLogger(UpdateRecordService::class.java)
        private val regex = "(?<=[A-Za-z0-9]{4}-[A-Za-z0-9]{6}-[A-Z]{2}-[0-9]{13}-)([A-Z]{2})(?=-[0-9]{13})".toRegex()
    }

    fun updateRecord(data: RequestRelease): UpdateResult<Fail> {
        val ocid = data.ocid
        val stage = extractStage(ocid)
            ?: return UpdateResult.error(
                DataErrors.Validation.DataMismatchToPattern(
                    name = "ocid",
                    pattern = regex.pattern,
                    actualValue = ocid
                )
            )

        val recordEntity = releaseService.tryGetRecordEntity(data.cpid, ocid)
            .doOnError { error -> return UpdateResult.error(error) }
            .get
            ?: return UpdateResult.error(Fail.Incident.Database.NotFound("Record not found."))

        val recordData = recordEntity.jsonData
        val record = jacksonJsonTransform.tryMapping(recordData, Record::class.java)
            .doOnError { error -> return UpdateResult.error(Fail.Incident.Database.InvalidData(recordData)) }
            .get

        val releaseId = releaseService.newReleaseId(ocid)
        val updatedRelease = record.updateRelease(releaseId = releaseId, received = data)
            .doReturn { e -> return UpdateResult.error(e) }
            .also {
                log.debug("UPDATED RELEASE (id: '${releaseId}'): '${toJson(it)}'.")
            }

        releaseService.saveRecord(
            cpId = data.cpid,
            stage = stage,
            record = updatedRelease,
            publishDate = recordEntity.publishDate
        )
        return UpdateResult.ok()
    }

    private fun extractStage(ocid: String): String? = regex.find(ocid)?.value
}
