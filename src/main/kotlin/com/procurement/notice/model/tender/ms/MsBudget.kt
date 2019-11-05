package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.BudgetBreakdown
import com.procurement.notice.model.ocds.Value

data class MsBudget @JsonCreator constructor(

    @Deprecated(level = DeprecationLevel.ERROR, message = "Not using")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @get:JsonProperty("isEuropeanUnionFunded")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val isEuropeanUnionFunded: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetBreakdown: List<BudgetBreakdown>?
)
