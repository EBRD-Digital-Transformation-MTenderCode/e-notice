package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Criterion @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val source: String?,

        val relatesTo: String?,

        val relatedItem: String?,

        val requirementGroups: HashSet<RequirementGroup>?
)