package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.Value

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MsBudget @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val amount: Value?,

        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        val budgetBreakdown: List<BudgetBreakdown>?
)