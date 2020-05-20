package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordOption(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("optionDetails") @param:JsonProperty("optionDetails") val optionDetails: String?
)
