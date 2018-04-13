package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("budget", "rationale", "documents", "milestones")
data class Planning(

        @JsonProperty("budget")
        val budget: Budget?,

        @JsonProperty("rationale")
        val rationale: String?,

        @JsonProperty("documents")
        val documents: List<Document>?,

        @JsonProperty("milestones")
        val milestones: List<Milestone>?
)