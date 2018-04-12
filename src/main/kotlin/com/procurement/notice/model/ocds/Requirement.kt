package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "title", "description", "dataType", "pattern", "expectedValue", "minValue", "maxValue", "period")
data class Requirement(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("dataType")
        val dataType: DataType?,

        @JsonProperty("pattern")
        val pattern: String?,

        @JsonProperty("expectedValue")
        val expectedValue: String?,

        @JsonProperty("minValue")
        val minValue: Double?,

        @JsonProperty("maxValue")
        val maxValue: Double?,

        @JsonProperty("period")
        val period: Period?
)

