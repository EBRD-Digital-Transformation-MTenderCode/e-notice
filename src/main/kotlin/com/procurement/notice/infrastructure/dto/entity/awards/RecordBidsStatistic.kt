package com.procurement.notice.infrastructure.dto.entity.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class RecordBidsStatistic(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("measure") @param:JsonProperty("measure") val measure: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Double?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("notes") @param:JsonProperty("notes") val notes: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedLot") @param:JsonProperty("relatedLot") val relatedLot: String?
)
