package com.procurement.notice.service

import com.procurement.notice.application.model.extractStage
import com.procurement.notice.application.model.parseCpid
import com.procurement.notice.application.model.parseOcid
import com.procurement.notice.domain.fail.Fail
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
    }

    fun updateRecord(data: RequestRelease): UpdateResult<Fail> {
        val ocid = parseOcid(data.ocid)
            .doReturn { error -> return UpdateResult.error(error) }

        val cpid = parseCpid(data.cpid)
            .doReturn { error -> return UpdateResult.error(error) }
            .toString()

        val stage = extractStage(ocid)
            .doReturn { error -> return UpdateResult.error(error) }

        val recordEntity = releaseService.tryGetRecordEntity(cpid, ocid.toString())
            .doOnError { error -> return UpdateResult.error(error) }
            .get
            ?: return UpdateResult.error(Fail.Incident.Database.NotFound("Record not found."))

        val recordData = recordEntity.jsonData
        val record = jacksonJsonTransform.tryMapping(recordData, Record::class.java)
            .doOnError { error -> return UpdateResult.error(Fail.Incident.Database.InvalidData(recordData)) }
            .get

        val releaseId = releaseService.newReleaseId(ocid.toString())
        val updatedRelease = record.updateRelease(releaseId = releaseId, received = data)
            .doReturn { e -> return UpdateResult.error(e) }
            .also {
                log.debug("UPDATED RELEASE (id: '${releaseId}'): '${toJson(it)}'.")
            }

        releaseService.saveRecord(
            cpId = data.cpid,
            stage = stage.toString(),
            record = updatedRelease,
            publishDate = recordEntity.publishDate
        )
        return UpdateResult.ok()
    }
}
