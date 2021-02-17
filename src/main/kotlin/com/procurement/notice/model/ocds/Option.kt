package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Option @JsonCreator constructor(

    @get:JsonProperty("hasOptions")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasOptions: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val optionDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: Period?
)
