package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("hasOptions", "optionDetails")
data class Option(

        @JsonProperty("hasOptions")
        val hasOptions: Boolean?,

        @JsonProperty("optionDetails")
        val optionDetails: String?
)