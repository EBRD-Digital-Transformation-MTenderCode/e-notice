package com.procurement.notice.infrastructure.dto

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.annotation.JsonValue
import com.procurement.notice.domain.fail.Fail
import java.time.LocalDateTime
import java.util.*

@JsonPropertyOrder("version", "id", "status", "result")
sealed class ApiResponse2(
    @field:JsonProperty("version") @param:JsonProperty("version") val version: ApiVersion2,
    @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
    @field:JsonProperty("result") @param:JsonProperty("result") val result: Any?
) {
    abstract val status: ResponseStatus
}

class ApiSuccessResponse(id: UUID, version: ApiVersion2, @JsonInclude(JsonInclude.Include.NON_NULL) result: Any?) :
    ApiResponse2(version = version, id = id, result = result) {
    @field:JsonProperty("status")
    override val status: ResponseStatus = ResponseStatus.SUCCESS
}

class ApiDataErrorResponse(
    version: ApiVersion2, id: UUID, result: List<Error>
) : ApiResponse2(version = version, result = result, id = id) {
    @field:JsonProperty("status")

    override val status: ResponseStatus = ResponseStatus.ERROR

    class Error(
        val code: String,
        val description: String,

        @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
        val details: List<Detail> = emptyList()
    ) {
        class Detail(
            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            val name: String? = null,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            val id: String? = null
        )
    }
}


class ApiFailResponse(version: ApiVersion2, id: UUID, result: List<Error>) :
    ApiResponse2(version = version, id = id, result = result) {

    @field:JsonProperty("status")
    override val status: ResponseStatus = ResponseStatus.ERROR

    class Error(val code: String, val description: String)
}

class ApiIncidentResponse(version: ApiVersion2, id: UUID, result: Incident) :
    ApiResponse2(version = version, result = result, id = id) {
    @field:JsonProperty("status")
    override val status: ResponseStatus = ResponseStatus.INCIDENT

    class Incident(
        val id: UUID,
        val level: Fail.Incident.Level,
        val date: LocalDateTime,
        val service: Service,
        val details: List<Details>
    ) {
        class Service(val id: String, val name: String, val version: String)
        class Details(
            val code: String,
            val description: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            val metadata: Any?
        )
    }
}

enum class ResponseStatus(private val value: String) {

    SUCCESS("success"),
    ERROR("error"),
    INCIDENT("incident");

    @JsonValue
    fun value(): String {
        return this.value
    }

    override fun toString(): String {
        return this.value
    }
}

