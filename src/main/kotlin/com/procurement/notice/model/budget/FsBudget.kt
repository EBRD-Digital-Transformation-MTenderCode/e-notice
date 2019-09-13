package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.EuropeanUnionFunding
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

data class FsBudget @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    val amount: Value,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val europeanUnionFunding: EuropeanUnionFunding?,

    @get:JsonProperty("isEuropeanUnionFunded")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isEuropeanUnionFunded: Boolean?,

    @get:JsonProperty("verified")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val verified: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val sourceEntity: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val project: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val projectID: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
