package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class RecordRecurrence(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("dates") @param:JsonProperty("dates") val dates: List<Date> = emptyList()
) {
    data class Date(
        @JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime?
    )
}
