package com.procurement.notice.infrastructure.dto.request.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.parties.RequestPerson
import java.time.LocalDateTime

data class RequestConfirmationResponse(
    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("date") @field:JsonProperty("date") val date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("requestId") @field:JsonProperty("requestId") val requestId: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("type") @field:JsonProperty("type") val type: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("value") @field:JsonProperty("value") val value: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("relatedPerson") @field:JsonProperty("relatedPerson") val relatedPerson: RequestPerson?
)
