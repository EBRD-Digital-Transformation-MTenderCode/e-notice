package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "relatedLots", "optionToCombine", "maximumValue")
data class LotGroup(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("relatedLots")
        val relatedLots: List<String>?,

        @JsonProperty("optionToCombine")
        @get:JsonProperty("optionToCombine")
        val optionToCombine: Boolean?,

        @JsonProperty("maximumValue")
        val maximumValue: Value?
)