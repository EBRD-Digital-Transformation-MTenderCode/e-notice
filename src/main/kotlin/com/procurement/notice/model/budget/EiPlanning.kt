package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder


@JsonPropertyOrder("budget", "rationale")
data class EiPlanning(

        @JsonProperty("budget")
        val budget: EiBudget?,

        @JsonProperty("rationale")
        val rationale: String?
)
