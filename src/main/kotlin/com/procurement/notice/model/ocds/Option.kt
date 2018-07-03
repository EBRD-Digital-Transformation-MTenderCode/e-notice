package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Option @JsonCreator constructor(

        @get:JsonProperty("hasOptions")
        val hasOptions: Boolean?,

        val optionDetails: String?
)