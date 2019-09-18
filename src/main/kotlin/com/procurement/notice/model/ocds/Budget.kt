package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Budget @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    private val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val project: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val projectID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val source: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val europeanUnionFunding: EuropeanUnionFunding?,

    @get:JsonProperty("isEuropeanUnionFunded")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val isEuropeanUnionFunded: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetBreakdown: List<BudgetBreakdown>?
)
