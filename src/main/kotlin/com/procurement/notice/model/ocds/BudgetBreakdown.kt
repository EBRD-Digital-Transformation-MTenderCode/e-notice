package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "description", "amount", "period", "sourceParty")
data class BudgetBreakdown(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("amount")
        val amount: Value?,

        @JsonProperty("period")
        val period: Period?,

        @JsonProperty("sourceParty")
        val sourceParty: OrganizationReference?
)
