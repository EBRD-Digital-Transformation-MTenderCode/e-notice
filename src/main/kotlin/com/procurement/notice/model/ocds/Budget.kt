package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "description", "amount", "project", "projectID", "uri", "source", "europeanUnionFunding", "isEuropeanUnionFunded", "budgetBreakdown")
data class Budget(

        @JsonProperty("id")
        private val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("amount")
        val amount: Value?,

        @JsonProperty("project")
        val project: String?,

        @JsonProperty("projectID")
        val projectID: String?,

        @JsonProperty("uri")
        val uri: String?,

        @JsonProperty("source")
        val source: String?,

        @JsonProperty("europeanUnionFunding")
        val europeanUnionFunding: EuropeanUnionFunding?,

        @JsonProperty("isEuropeanUnionFunded")
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<BudgetBreakdown>?
)
