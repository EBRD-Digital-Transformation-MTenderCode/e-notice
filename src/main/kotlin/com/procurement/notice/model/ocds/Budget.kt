package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Budget @JsonCreator constructor(

        private val id: String?,

        val description: String?,

        val amount: Value?,

        val project: String?,

        val projectID: String?,

        val uri: String?,

        val source: String?,

        val europeanUnionFunding: EuropeanUnionFunding?,

        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        val budgetBreakdown: List<BudgetBreakdown>?
)
