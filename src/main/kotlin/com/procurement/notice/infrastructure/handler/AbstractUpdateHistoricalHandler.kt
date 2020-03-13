package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.extention.toList
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.infrastructure.dto.ApiDataErrorResponse
import com.procurement.notice.infrastructure.dto.ApiErrorResponse
import com.procurement.notice.infrastructure.dto.ApiFailResponse
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiSuccessResponse
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.model.bpe.getFullErrorCode
import com.procurement.notice.model.bpe.tryGetId
import com.procurement.notice.model.bpe.tryGetVersion
import com.procurement.notice.model.entity.HistoryEntity
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.tryToObject
import java.time.LocalDateTime
import java.util.*

abstract class AbstractUpdateHistoricalHandler<ACTION : Action, E : Fail>(
    private val logger: Logger,
    private val historyDao: HistoryDao
) : Handler<ACTION, ApiResponse2> {

    override fun handle(node: JsonNode): ApiResponse2 {
        val id = node.tryGetId().get
        val version = node.tryGetVersion().get

        val history: HistoryEntity? = historyDao.getHistory(id.toString(), action.key)
        if (history != null) {
            val data = history.jsonData
            val result = data.tryToObject(ApiSuccessResponse::class.java)
                .doOnError {
                    return generateResponseOnFailure(
                        fail = Fail.Incident.Database.InvalidData(data),
                        id = id,
                        version = version
                    )
                }
            return result.get
        }

        val result: ApiResponse2 = execute(node)
        return when(result) {
            is ApiSuccessResponse -> {
                historyDao.saveHistory(result.id.toString(), action.key, result)
                logger.info("${action.key} has been executed. Result: ${toJson(result)}")
                result
            }
            is ApiDataErrorResponse,
            is ApiFailResponse,
            is ApiErrorResponse,
            is ApiIncidentResponse -> result
        }
    }

    abstract fun execute(node: JsonNode): ApiResponse2

    fun generateResponseOnFailure(fail: Fail, version: ApiVersion2, id: UUID): ApiResponse2 {
        fail.logging(logger)

        return when (fail) {
            is DataErrors.Validation ->
                ApiDataErrorResponse(
                    version = version,
                    id = id,
                    result = fail.let { dataError ->
                        ApiDataErrorResponse.Error(
                            code = getFullErrorCode(dataError.code),
                            description = dataError.description,
                            attributeName = dataError.name
                        )
                    }.toList()
                )
            is Fail.Error            ->
                ApiFailResponse(
                    version = version,
                    id = id,
                    result = fail.let { error ->
                        ApiFailResponse.Error(
                            code = getFullErrorCode(error.code),
                            description = error.description
                        )
                    }.toList()
                )
            is Fail.Incident         -> {
                val errors = fail.let { incident ->
                    ApiIncidentResponse.Incident.Error(
                        code = getFullErrorCode(incident.code),
                        description = incident.description,
                        metadata = null
                    )
                }.toList()
                generateIncident(errors, version, id)
            }
        }
    }

    private fun generateIncident(
        errors: List<ApiIncidentResponse.Incident.Error>, version: ApiVersion2, id: UUID
    ): ApiIncidentResponse =
        ApiIncidentResponse(
            version = version,
            id = id,
            result = ApiIncidentResponse.Incident(
                date = LocalDateTime.now(),
                id = UUID.randomUUID(),
                service = ApiIncidentResponse.Incident.Service(
                    id = GlobalProperties.service.id,
                    version = GlobalProperties.service.version,
                    name = GlobalProperties.service.name
                ),
                errors = errors
            )
        )


}

