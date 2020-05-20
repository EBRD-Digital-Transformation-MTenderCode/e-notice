package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class ReleasePreQualification(
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period
) {
    data class Period(
        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime?,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime?
    )
}
