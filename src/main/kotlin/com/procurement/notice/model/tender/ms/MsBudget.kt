package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.Value


@JsonPropertyOrder("id", "description", "amount", "isEuropeanUnionFunded", "europeanUnionFunding", "budgetBreakdown")
data class MsBudget(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("amount")
        val amount: Value?,
        @JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @JsonProperty("europeanUnionFunding")
        val europeanUnionFunding: EuropeanUnionFunding?,

        @JsonProperty("budgetBreakdown")
        val budgetBreakdown: List<BudgetBreakdown>?
)