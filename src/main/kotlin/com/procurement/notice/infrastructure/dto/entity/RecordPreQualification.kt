package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

class RecordPreQualification(
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period
) {
    data class Period(
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
    )
}
