package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

data class FsBudget @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val period: Period?,

        val amount: Value,

        val europeanUnionFunding: EuropeanUnionFunding?,

        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @get:JsonProperty("verified")
        val verified: Boolean?,

        val sourceEntity: OrganizationReference?
)
