package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.request.RequestRelease
import com.procurement.notice.infrastructure.service.record.updateRelease
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.getParams
import com.procurement.notice.service.ReleaseService
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class UpdateRecordHandler(
    private val releaseService: ReleaseService,
    historyDao: HistoryDao
) : AbstractUpdateHistoricalHandler<CommandType2, UpdateError>(historyDao) {

    companion object {
        private val log = LoggerFactory.getLogger(UpdateRecordHandler::class.java)
        private val regex = "(?<=[A-Za-z0-9]{4}-[A-Za-z0-9]{6}-[A-Z]{2}-[0-9]{13}-)([A-Z]{2})(?=-[0-9]{13})".toRegex()
    }

    override val action: CommandType2
        get() = CommandType2.UPDATE_RECORD

    override fun execute(node: JsonNode): UpdateResult<UpdateError> {
        val request = node.getParams(RequestRelease::class.java)
        val ocid = request.ocid ?: throw ErrorException(
            error = ErrorType.OCID_ERROR,
            message = "Missing 'ocid' field in request payload"
        )
        val stage = extractStage(ocid) ?: throw ErrorException(
            error = ErrorType.OCID_ERROR,
            message = "Cannot extract stage from ocid '${ocid}'. "
        )

        val recordEntity = releaseService.getRecordEntity(request.cpid!!, ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        val updatedRelease = updateRelease(
            releaseService.newReleaseId(ocid),
            request,
            record
        )
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
