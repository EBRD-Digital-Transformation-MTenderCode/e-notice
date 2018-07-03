package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.Value

data class MsBudget @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val amount: Value?,

        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        val europeanUnionFunding: EuropeanUnionFunding?,

        val budgetBreakdown: List<BudgetBreakdown>?
)