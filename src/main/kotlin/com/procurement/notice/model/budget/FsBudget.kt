package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

data class FsBudget @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    val amount: Value,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val europeanUnionFunding: EuropeanUnionFunding?,

    @get:JsonProperty("isEuropeanUnionFunded")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val isEuropeanUnionFunded: Boolean?,

    @get:JsonProperty("verified")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val verified: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val sourceEntity: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val project: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val projectID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
