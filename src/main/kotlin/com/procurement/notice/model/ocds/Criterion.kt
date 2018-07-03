package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Criterion @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val source: Source?,

        val relatesTo: RelatesTo?,

        val relatedItem: String?,

        val requirementGroups: HashSet<RequirementGroup>?
)