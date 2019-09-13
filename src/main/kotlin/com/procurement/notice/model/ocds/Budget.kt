package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Budget @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val project: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val projectID: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val source: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val europeanUnionFunding: EuropeanUnionFunding?,

    @get:JsonProperty("isEuropeanUnionFunded")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isEuropeanUnionFunded: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetBreakdown: List<BudgetBreakdown>?
)
