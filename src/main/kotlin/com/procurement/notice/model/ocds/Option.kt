package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Option @JsonCreator constructor(

        @get:JsonProperty("hasOptions")
        val hasOptions: Boolean?,

        val optionDetails: String?
)