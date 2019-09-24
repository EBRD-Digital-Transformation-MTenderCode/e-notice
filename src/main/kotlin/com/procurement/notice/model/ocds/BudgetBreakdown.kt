package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class BudgetBreakdown @JsonCreator constructor(

    val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val sourceParty: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val europeanUnionFunding: EuropeanUnionFunding?
)
