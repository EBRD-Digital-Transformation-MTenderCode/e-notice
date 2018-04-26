package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("isRecurrent", "dates", "description")
data class RecurrentProcurement(

        @JsonProperty("isRecurrent")
        @get:JsonProperty("isRecurrent")
        val isRecurrent: Boolean?,

        @JsonProperty("dates")
        val dates: List<Period>?,

        @JsonProperty("description")
        val description: String?
)