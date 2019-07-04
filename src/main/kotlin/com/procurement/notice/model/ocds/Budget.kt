package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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
