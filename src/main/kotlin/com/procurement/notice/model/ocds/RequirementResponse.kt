package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class RequirementResponse @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val value: String?,

        val period: Period?,

        val requirement: RequirementReference?,

        val relatedTenderer: OrganizationReference?
)