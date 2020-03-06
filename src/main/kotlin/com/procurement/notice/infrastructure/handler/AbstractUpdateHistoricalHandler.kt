package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.infrastructure.dto.ApiFailResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiSuccessResponse
import com.procurement.notice.model.bpe.tryGetId
import com.procurement.notice.model.bpe.tryGetVersion
import com.procurement.notice.model.entity.HistoryEntity
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.slf4j.LoggerFactory

abstract class AbstractUpdateHistoricalHandler<ACTION : Action, E : DataErrors>(
    private val historyDao: HistoryDao
) : Handler<ACTION, ApiResponse2> {
    companion object {
        private val log = LoggerFactory.getLogger(AbstractUpdateHistoricalHandler::class.java)
    }

    override fun handle(node: JsonNode): ApiResponse2 {
        val id = node.tryGetId().get
        val version = node.tryGetVersion().get

        val history: HistoryEntity? = historyDao.getHistory(id.toString(), action.value)
        if (history != null) {
            return toObject(ApiSuccessResponse::class.java, history.jsonData)
        }

        val result: UpdateResult<DataErrors> = execute(node)
        return when(result) {
            is UpdateResult.Ok -> {
                ApiSuccessResponse(version = version, id = id, result = result)
                    .also { response ->
                        historyDao.saveHistory(response.id.toString(), action.value, response)
                        if (log.isDebugEnabled)
                            log.debug("${action.value} has been executed. Result: ${toJson(response)}")
                    }
            }
            is UpdateResult.Error -> {
                ApiFailResponse(version = version, id = id, result = listOf(result.value)
                    .map {
                        ApiFailResponse.Error(
                            code = it.code,
                            description = it.description
                        )
                    })
            }
        }
    }

    abstract fun execute(node: JsonNode): UpdateResult<DataErrors>
}

