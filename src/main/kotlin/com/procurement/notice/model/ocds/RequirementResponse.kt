package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RequirementResponse @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val value: String?,

        val period: Period?,

        val requirement: RequirementReference?,

        val relatedTenderer: OrganizationReference?
)