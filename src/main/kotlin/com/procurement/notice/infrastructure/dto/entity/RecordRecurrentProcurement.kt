package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordRecurrentProcurement(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isRecurrent") @param:JsonProperty("isRecurrent") val isRecurrent: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("dates") @param:JsonProperty("dates") val dates: List<RecordPeriod> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
)
