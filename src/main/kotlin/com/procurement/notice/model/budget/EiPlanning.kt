package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("budget", "rationale")
data class EiPlanning(

        @JsonProperty("budget")
        val budget: EiBudget?,

        @JsonProperty("rationale")
        val rationale: String?
)
