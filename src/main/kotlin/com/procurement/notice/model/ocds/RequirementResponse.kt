package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class RequirementResponse @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val requirement: RequirementReference?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedTenderer: OrganizationReference?
)
