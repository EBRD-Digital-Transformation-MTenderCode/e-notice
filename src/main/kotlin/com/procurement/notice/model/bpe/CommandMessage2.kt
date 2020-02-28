package com.procurement.notice.model.bpe

import com.fasterxml.jackson.annotation.JsonValue
import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.node.NullNode
import com.procurement.notice.config.properties.GlobalProperties
import com.procurement.notice.exception.EnumException
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.infrastructure.dto.Action
import com.procurement.notice.infrastructure.dto.ApiFailResponse2
import com.procurement.notice.infrastructure.dto.ApiIncidentResponse2
import com.procurement.notice.infrastructure.dto.ApiResponse2
import com.procurement.notice.infrastructure.dto.ApiVersion2
import com.procurement.notice.utils.toObject
import java.time.LocalDateTime
import java.util.*

enum class CommandType2(override val value: String): Action {
    UPDATE_RECORD("updateRecord");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}

fun errorResponse2(exception: Exception, id: UUID = NaN, version: ApiVersion2): ApiResponse2 =
    when (exception) {
        is ErrorException -> ApiFailResponse2(
            id = id,
            version = version,
            result = listOf(
                ApiFailResponse2.Error(
                    code = getFullErrorCode(exception.code),
                    description = exception.message!!
                )
            )
        )
        is EnumException -> ApiFailResponse2(
            id = id,
            version = version,
            result = listOf(
                ApiFailResponse2.Error(
                    code = getFullErrorCode(exception.code),
                    description = exception.message!!
                )
            )
        )
        else -> ApiIncidentResponse2(
            id = id,
            version = version,
            result = createIncident("00.00", exception.message ?: "Internal server error.")
        )
    }

fun createIncident(code: String, message: String, metadata: Any? = null): ApiIncidentResponse2.Incident {
    return ApiIncidentResponse2.Incident(
        date = LocalDateTime.now(),
        id = UUID.randomUUID(),
        service = ApiIncidentResponse2.Incident.Service(
            id = GlobalProperties.serviceId,
            version = GlobalProperties.App.apiVersion,
            name = GlobalProperties.serviceName
        ),
        errors = listOf(
            ApiIncidentResponse2.Incident.Error(
                code = getFullErrorCode(code),
                description = message,
                metadata = metadata
            )
        )
    )
}

private fun getFullErrorCode(code: String): String = "400.${GlobalProperties.serviceId}." + code

val NaN: UUID
    get() = UUID(0, 0)

fun JsonNode.getBy(parameter: String): JsonNode {
    val node = get(parameter)
    if(node == null || node is NullNode)  throw IllegalArgumentException("$parameter is absent")
    return node
}

fun JsonNode.getId() = UUID.fromString(getBy("id").asText())
fun JsonNode.getVersion() = ApiVersion2.valueOf(getBy("version").asText())
fun JsonNode.getAction() = getBy("action")
fun <T: Any> JsonNode.getParams(clazz: Class<T>) = getBy("params").toObject(clazz)