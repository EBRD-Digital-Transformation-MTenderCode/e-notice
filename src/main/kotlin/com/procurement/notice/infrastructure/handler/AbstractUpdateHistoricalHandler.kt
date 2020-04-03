package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.dao.HistoryDao
import com.procurement.notice.domain.extention.toList
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataValidationErrors
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.infrastructure.dto.ApiDataErrorResponse
import com.procurement.notice.infrastructure.dto.ApiFailResponse
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiSuccessResponse
import com.procurement.notice.infrastructure.dto.ApiVersion2
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

        val result: UpdateResult<Fail> = execute(node)

        return when (result) {
            is UpdateResult.Ok    -> ApiSuccessResponse(id = id, version = version, result = result)
                .also {
                    historyDao.saveHistory(id.toString(), action.key, it)
                    logger.info("${action.key} has been executed. Result: ${toJson(it)}")
                }
            is UpdateResult.Error -> generateResponseOnFailure(id = id, version = version, fail = result.value)
        }
    }

    abstract fun execute(node: JsonNode): UpdateResult<Fail>

    fun generateResponseOnFailure(fail: Fail, version: ApiVersion2, id: UUID): ApiResponse2 {
        fail.logging(logger)

        return when (fail) {
            is DataValidationErrors ->
                ApiDataErrorResponse(
                    version = version,
                    id = id,
                    result = fail.let { dataError ->
                        ApiDataErrorResponse.Error(
                            code = dataError.code,
                            description = dataError.description,
                            details = listOf(
                                ApiDataErrorResponse.Error.Detail(name = fail.name)
                            )
                        )
                    }.toList()
                )
            is Fail.Error            ->
                ApiFailResponse(
                    version = version,
                    id = id,
                    result = fail.let { error ->
                        ApiFailResponse.Error(
                            code = error.code,
                            description = error.description
                        )
                    }.toList()
                )
            is Fail.Incident         -> {
                val errors = fail.let { incident ->
                    ApiIncidentResponse.Incident.Details(
                        code = incident.code,
                        description = incident.description,
                        metadata = null
                    )
                }.toList()
                generateIncident(errors, version, id)
            }
        }
    }

    private fun generateIncident(
        errors: List<ApiIncidentResponse.Incident.Details>, version: ApiVersion2, id: UUID
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
                details = errors
            )
        )
}

