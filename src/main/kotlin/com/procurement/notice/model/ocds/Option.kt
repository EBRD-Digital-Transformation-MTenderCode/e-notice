package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Option @JsonCreator constructor(

    @get:JsonProperty("hasOptions")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasOptions: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val optionDetails: String?
)