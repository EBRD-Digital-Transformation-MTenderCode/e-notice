package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isRecurrent", "dates", "description")
data class RecurrentProcurement(

        @JsonProperty("isRecurrent")
        val isRecurrent: Boolean?,

        @JsonProperty("dates")
        val dates: List<Period>?,

        @JsonProperty("description")
        val description: String?
)