package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.handler.record.create.CreateRecordHandler
import com.procurement.notice.infrastructure.handler.record.update.UpdateRecordHandler
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.errorResponse
import com.procurement.notice.model.bpe.tryGetAction
import com.procurement.notice.model.bpe.tryGetId
import com.procurement.notice.model.bpe.tryGetVersion
import com.procurement.notice.utils.toJson
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CommandService2(
    private val updateRecordHandler: UpdateRecordHandler,
    private val createRecordHandler: CreateRecordHandler,
    private val logger: Logger
) {
    companion object {
        private val log = LoggerFactory.getLogger(CommandService2::class.java)
    }

    fun execute(request: JsonNode): ApiResponse2 {

        val id = request.tryGetId()
            .doReturn { error -> return errorResponse(fail = error, logger = logger) }

        val version = request.tryGetVersion()
            .doReturn { error -> return errorResponse(fail = error, id = id, logger = logger) }

        val action = request.tryGetAction()
            .doReturn { error -> return errorResponse(fail = error, id = id, version = version, logger = logger) }

        val response = when (action) {
            CommandType2.UPDATE_RECORD -> updateRecordHandler.handle(node = request)
            CommandType2.CREATE_RECORD -> createRecordHandler.handle(node = request)
        }

        if (log.isDebugEnabled)
            log.debug("DataOfResponse: '${toJson(response)}'.")

        return response
    }
}

