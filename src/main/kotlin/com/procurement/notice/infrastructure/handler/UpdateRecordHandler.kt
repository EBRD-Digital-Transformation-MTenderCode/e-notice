package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.infrastructure.dto.entity.Record
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.infrastructure.service.record.updateRelease
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.tryGetParams
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.tryToObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UpdateRecordHandler(
    private val releaseService: ReleaseService,
    historyDao: HistoryDao
) : AbstractUpdateHistoricalHandler<CommandType2, Fail>(historyDao) {

    companion object {
        private val log = LoggerFactory.getLogger(UpdateRecordHandler::class.java)
        private val regex = "(?<=[A-Za-z0-9]{4}-[A-Za-z0-9]{6}-[A-Z]{2}-[0-9]{13}-)([A-Z]{2})(?=-[0-9]{13})".toRegex()
    }

    override val action: CommandType2
        get() = CommandType2.UPDATE_RECORD

    override fun execute(node: JsonNode): UpdateResult<Fail> {
        val request = node.tryGetParams(RequestRelease::class.java)
            .doOnError { error: DataErrors -> return UpdateResult.error(error) }
            .get

        val ocid = request.ocid
        val stage = extractStage(ocid) ?: return UpdateResult.error(
            DataErrors.Validation.DataMismatchToPattern(
                name = "ocid",
                pattern = regex.pattern,
                actualValue = ocid
            )
        )

        val recordEntity = releaseService.tryGetRecordEntity(request.cpid, ocid)
            .doOnError { error -> return UpdateResult.error(error) }
            .get
            ?: return UpdateResult.error(Fail.Incident.Database.NotFound("Record not found."))

        val recordData = recordEntity.jsonData
        val record = recordData.tryToObject(Record::class.java)
            .doOnError { error -> return UpdateResult.error(Fail.Incident.InternalError()) }
            .get

        val releaseId = releaseService.newReleaseId(ocid)
        val updatedRelease = record.updateRelease(
            releaseId = releaseId,
            received = request
        )
            .doReturn { e -> return UpdateResult.error(e) }
            .also {
                log.debug("UPDATED RELEASE (id: '${releaseId}'): '${toJson(it)}'.")
            }

        releaseService.saveRecord(
            cpId = request.cpid,
            stage = stage,
            record = updatedRelease,
            publishDate = recordEntity.publishDate
        )

        return UpdateResult.ok()
    }

    private fun extractStage(ocid: String): String? = regex.find(ocid)?.value
}
