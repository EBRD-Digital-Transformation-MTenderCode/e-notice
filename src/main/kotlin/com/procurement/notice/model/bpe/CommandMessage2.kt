package com.procurement.notice.model.bpe

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.domain.fail.Fail
import com.procurement.notice.domain.fail.error.DataErrors
import com.procurement.notice.domain.utils.Action
import com.procurement.notice.domain.utils.Result
import com.procurement.notice.domain.utils.ValidationResult
import com.procurement.notice.domain.utils.bind
import com.procurement.notice.infrastructure.dto.ApiErrorResponse
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.utils.toObject
import com.procurement.notice.utils.tryToObject
import java.time.LocalDateTime
import java.util.*

enum class CommandType2(@JsonValue override val value: String) : Action {
    UPDATE_RECORD("updateRecord");

    companion object {
        private val elements: Map<String, CommandType2> = values().associateBy { it.value.toUpperCase() }

        fun tryOf(value: String): Result<CommandType2, String> = elements[value.toUpperCase()]
            ?.let {
                Result.success(it)
            }
            ?: Result.failure(
                "Unknown value for enumType ${CommandType2::class.java.canonicalName}: " +
                    "$value, Allowed values are ${values().joinToString { it.value }}"
            )
    }

    override fun toString(): String = this.value
}

fun errorResponse(fail: Fail, id: UUID = NaN, version: ApiVersion2): ApiResponse2 =
    when (fail) {
        is Fail.Error    -> getApiFailResponse(
            id = id,
            version = version,
            code = fail.code,
            message = fail.description
        )
        is Fail.Incident -> getApiIncidentResponse(
            id = id,
            version = version,
            code = fail.code,
            message = fail.description
        )
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

val NaN: UUID
    get() = UUID(0, 0)

fun JsonNode.tryGetAttribute(name: String): Result<JsonNode, DataErrors> {
    val node = get(name) ?: return Result.failure(
        DataErrors.MissingRequiredAttribute(name)
    )
    if (node is NullNode) return Result.failure(
        DataErrors.DataTypeMismatch(name)
    )

    return Result.success(node)
}

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
                    DataErrors.DataFormatMismatch(attributeName = result.error)
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
                    DataErrors.DataFormatMismatch(attributeName = result.error)
                }
            }
        }
}

fun <T : Any> JsonNode.getParams(clazz: Class<T>) = getBy("params").toObject(clazz)

private fun asUUID(value: String): Result<UUID, DataErrors> =
    try {
        Result.success<UUID>(UUID.fromString(value))
    } catch (exception: IllegalArgumentException) {
        Result.failure(
            DataErrors.DataFormatMismatch(attributeName = "id")
        )
    }

fun JsonNode.getAttribute(name: String): Result<JsonNode, DataErrors> {
    return if (has(name)) {
        val attr = get(name)
        if (attr !is NullNode)
            Result.success(attr)
        else
            Result.failure(
                DataErrors.DataTypeMismatch(attributeName = "$attr")
            )
    } else
        Result.failure(
            DataErrors.MissingRequiredAttribute(attributeName = name)
        )
}

fun <T : Any> JsonNode.tryGetParams(target: Class<T>): Result<T, DataErrors> =
    getAttribute("params").bind { node ->
        when (val result = node.tryToObject(target)) {
            is Result.Success -> result
            is Result.Failure -> result.mapError {
                DataErrors.DataFormatMismatch(attributeName = result.error)
            }
        }
    }

fun JsonNode.hasParams(): ValidationResult<DataErrors> =
    if (this.has("params"))
        ValidationResult.ok()
    else
        ValidationResult.error(
            DataErrors.MissingRequiredAttribute(attributeName = "params")
        )

