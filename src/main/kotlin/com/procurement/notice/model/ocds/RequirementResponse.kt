package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class RequirementResponse @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val requirement: RequirementReference?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedTenderer: OrganizationReference?
)