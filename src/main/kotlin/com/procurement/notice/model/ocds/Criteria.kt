package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude

data class Criteria(
    val id: String,
    val title: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    val requirementGroups: List<RequirementGroup>,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatesTo: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?
)