package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("rationale", "budget")
data class MsPlanning(

        @JsonProperty("budget")
        val budget: MsBudget?,

        @JsonProperty("rationale")
        val rationale: String?

)
