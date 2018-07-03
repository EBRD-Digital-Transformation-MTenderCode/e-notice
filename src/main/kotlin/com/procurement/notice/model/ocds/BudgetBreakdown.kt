package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class BudgetBreakdown @JsonCreator constructor(

        val id: String,

        val description: String?,

        val amount: Value?,

        val period: Period?,

        val sourceParty: OrganizationReference?
)
