package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "id",
        "description",
        "period",
        "amount",
        "europeanUnionFunding",
        "isEuropeanUnionFunded",
        "budgetBreakdown",
        "verified",
        "sourceEntity")
data class FsBudget(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("period")
        val period: Period?,

        @JsonProperty("amount")
        val amount: Value,

        @JsonProperty("europeanUnionFunding")
        val europeanUnionFunding: EuropeanUnionFunding?,

        @JsonProperty("isEuropeanUnionFunded")
        @get:JsonProperty("isEuropeanUnionFunded")
        val isEuropeanUnionFunded: Boolean?,

        @JsonProperty("verified")
        @get:JsonProperty("verified")
        val verified: Boolean?,

        @JsonProperty("sourceEntity")
        val sourceEntity: OrganizationReference?
)
