package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.handler.UpdateRecordHandler
import com.procurement.notice.model.bpe.CommandType2
import com.procurement.notice.model.bpe.getAction
import com.procurement.notice.utils.toObject
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class CommandService2(
    private val updateRecordHandler: UpdateRecordHandler
) {
    companion object {
        private val log = LoggerFactory.getLogger(CommandService2::class.java)
    }

    fun execute(node: JsonNode): ApiResponse2 {
        val action = node.getAction().toObject(CommandType2::class.java)
        return when (action) {
            CommandType2.UPDATE_RECORD -> updateRecordHandler.handle(node)
        }
    }
}

