package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestRecurrentProcurement(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isRecurrent") @param:JsonProperty("isRecurrent") val isRecurrent: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("dates") @param:JsonProperty("dates") val dates: List<RequestPeriod> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
)
