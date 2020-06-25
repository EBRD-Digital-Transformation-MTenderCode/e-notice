package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class RecordPreQualification(
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period
) {
    data class Period(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime?,
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime?
    )
}
