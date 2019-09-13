package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class BudgetBreakdown @JsonCreator constructor(

    val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val sourceParty: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val europeanUnionFunding: EuropeanUnionFunding?
)
