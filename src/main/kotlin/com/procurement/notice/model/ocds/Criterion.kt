package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Criterion @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val source: Source?,

        val relatesTo: RelatesTo?,

        val relatedItem: String?,

        val requirementGroups: HashSet<RequirementGroup>?
)