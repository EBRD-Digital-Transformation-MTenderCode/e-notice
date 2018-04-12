package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "type", "description", "amount", "estimationMethod")
data class ValueBreakdown(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("type")
        val type: List<ValueBreakdownType>?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("amount")
        val amount: Value?,

        @JsonProperty("estimationMethod")
        val estimationMethod: Value?
)
