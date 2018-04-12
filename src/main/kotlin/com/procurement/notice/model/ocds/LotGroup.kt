package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "relatedLots", "optionToCombine", "maximumValue")
data class LotGroup(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("relatedLots")
        val relatedLots: List<String>?,

        @JsonProperty("optionToCombine")
        val optionToCombine: Boolean?,

        @JsonProperty("maximumValue")
        val maximumValue: Value?
)