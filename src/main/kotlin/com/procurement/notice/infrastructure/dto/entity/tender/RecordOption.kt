package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordPeriod

data class RecordOption(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("optionDetails") @param:JsonProperty("optionDetails") val optionDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?
)
