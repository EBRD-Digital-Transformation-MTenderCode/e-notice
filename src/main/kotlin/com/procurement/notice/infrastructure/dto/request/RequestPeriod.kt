package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class RequestPeriod(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maxExtentDate") @param:JsonProperty("maxExtentDate") val maxExtentDate: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("durationInDays") @param:JsonProperty("durationInDays") val durationInDays: Int?
)
