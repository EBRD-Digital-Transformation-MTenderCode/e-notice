package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("budget", "rationale")
data class FsPlanning(

        @JsonProperty("budget")
        val budget: FsBudget?,

        @JsonProperty("rationale")
        val rationale: String?
)
