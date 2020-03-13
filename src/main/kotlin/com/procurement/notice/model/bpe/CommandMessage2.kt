package com.procurement.notice.model.bpe

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.JsonNodeType
import com.fasterxml.jackson.databind.node.NullNode
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.domain.utils.EnumElementProvider
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.bind
import com.procurement.notice.infrastructure.dto.ApiErrorResponse
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.infrastructure.extention.tryGetAttribute
import java.time.LocalDateTime
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

fun errorResponse(fail: Fail, id: UUID = NaN, version: ApiVersion2 = GlobalProperties.App.apiVersion): ApiResponse2 =
    when (fail) {
        is Fail.Error    ->
            getApiFailResponse(id = id, version = version, code = fail.code, message = fail.description)
        is Fail.Incident ->
            getApiIncidentResponse(id = id, version = version, code = fail.code, message = fail.description)
    }

private fun getApiFailResponse(
    id: UUID,
    version: ApiVersion2,
    code: String,
    message: String
): ApiErrorResponse {
    return ApiErrorResponse(
        id = id,
        version = version,
        result = listOf(
            ApiErrorResponse.Error(
                code = "${code}/${GlobalProperties.service.id}",
                description = message
            )
        )
    )
}

private fun getApiIncidentResponse(
    id: UUID,
    version: ApiVersion2,
    code: String,
    message: String
): ApiIncidentResponse {
    return ApiIncidentResponse(
        id = id,
        version = version,
        result = ApiIncidentResponse.Incident(
            id = UUID.randomUUID(),
            date = LocalDateTime.now(),
            errors = listOf(
                ApiIncidentResponse.Incident.Error(
                    code = "${code}/${GlobalProperties.service.id}",
                    description = message,
                    metadata = null
                )
            ),
            service = ApiIncidentResponse.Incident.Service(
                id = GlobalProperties.service.id,
                version = GlobalProperties.service.version,
                name = GlobalProperties.service.name
            )
        )
    )
}

fun getFullErrorCode(code: String): String = "${code}/${GlobalProperties.service.id}"

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
            when (val result = ApiVersion2.tryOf(value)) {
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

