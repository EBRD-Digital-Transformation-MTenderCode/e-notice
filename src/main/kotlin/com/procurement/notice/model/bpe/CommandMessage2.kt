package com.procurement.notice.model.bpe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.NullNode
import com.procurement.notice.application.service.Logger
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.domain.extention.nowDefaultUTC
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.bind
import com.procurement.notice.infrastructure.dto.ApiDataErrorResponse
import com.procurement.notice.infrastructure.dto.ApiFailResponse
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.infrastructure.extention.tryGetAttribute
import java.util.*

enum class CommandType2(@JsonValue override val key: String) : EnumElementProvider.Key, Action {

    UPDATE_RECORD("updateRecord");

    override fun toString(): String = key

    companion object : EnumElementProvider<CommandType2>(info = info()) {

        @JvmStatic
        @JsonCreator
        fun creator(name: String) = CommandType2.orThrow(name)
    }
}

fun errorResponse(
    fail: Fail,
    id: UUID = NaN,
    version: ApiVersion2 = GlobalProperties.App.apiVersion,
    logger: Logger
): ApiResponse2 {
    fail.logging(logger)
    return when (fail) {
        is Fail.Error -> {
            when (fail) {
                is DataErrors.Validation ->
                    ApiDataErrorResponse(
                        version = version,
                        id = id,
                        result = listOf(
                            ApiDataErrorResponse.Error(
                                code = fail.code,
                                description = fail.description,
                                details = listOf(
                                    ApiDataErrorResponse.Error.Detail(name = fail.name)
                                )
                            )
                        )
                    )
                else -> ApiFailResponse(
                    version = version,
                    id = id,
                    result = listOf(
                        ApiFailResponse.Error(
                            code = fail.code,
                            description = fail.description
                        )
                    )
                )
            }
        }
        is Fail.Incident -> {
            val errors = listOf(
                ApiIncidentResponse.Incident.Details(
                    code = fail.code,
                    description = fail.description,
                    metadata = null
                )
            )
            generateIncident(errors, version, id)
        }
    }
}

private fun generateIncident(
    details: List<ApiIncidentResponse.Incident.Details>, version: ApiVersion2, id: UUID
): ApiIncidentResponse =
    ApiIncidentResponse(
        version = version,
        id = id,
        result = ApiIncidentResponse.Incident(
            date = nowDefaultUTC(),
            id = UUID.randomUUID(),
            service = ApiIncidentResponse.Incident.Service(
                id = GlobalProperties.service.id,
                version = GlobalProperties.service.version,
                name = GlobalProperties.service.name
            ),
            details = details
        )
    )

val NaN: UUID
    get() = UUID(0, 0)

fun JsonNode.getBy(parameter: String): JsonNode {
    val node = get(parameter)
    if (node == null || node is NullNode) throw IllegalArgumentException("$parameter is absent")
    return node
}

fun JsonNode.tryGetId(): Result<UUID, DataErrors> {
    return this.getAttribute("id")
        .bind {
            val value = it.asText()
            asUUID(value)
        }
}

fun JsonNode.tryGetVersion(): Result<ApiVersion2, DataErrors> {
    return this.getAttribute("version")
        .bind {
            val value = it.asText()
            when (val result = ApiVersion2.tryValueOf(value)) {
                is Result.Success -> result
                is Result.Failure -> result.mapError {
                    DataErrors.Validation.DataFormatMismatch(
                        name = "version",
                        actualValue = value,
                        expectedFormat = "00.00.00"
                    )
                }
            }
        }
}

fun JsonNode.tryGetAction(): Result<CommandType2, DataErrors> {
    return this.getAttribute("action")
        .bind {
            val value = it.asText()
            when (val result = CommandType2.tryOf(value)) {
                is Result.Success -> result
                is Result.Failure -> result.mapError {
                    DataErrors.Validation.UnknownValue(
                        name = "action",
                        actualValue = value,
                        expectedValues = CommandType2.allowedValues
                    )
                }
            }
        }
}

private fun asUUID(value: String): Result<UUID, DataErrors> =
    try {
        Result.success<UUID>(UUID.fromString(value))
    } catch (exception: IllegalArgumentException) {
        Result.failure(
            DataErrors.Validation.DataFormatMismatch(
                name = "id",
                expectedFormat = "uuid",
                actualValue = value
            )
        )
    }

fun JsonNode.getAttribute(name: String): Result<JsonNode, DataErrors> {
    return if (has(name)) {
        val attr = get(name)
        if (attr !is NullNode)
            Result.success(attr)
        else
            Result.failure(
                DataErrors.Validation.DataTypeMismatch(name = "$attr", actualType = "null", expectedType = "not null")
            )
    } else
        Result.failure(
            DataErrors.Validation.MissingRequiredAttribute(name = name)
        )
}

fun JsonNode.tryGetParams(): Result<JsonNode, DataErrors> =
    this.tryGetAttribute(name = "params", type = JsonNodeType.OBJECT)

