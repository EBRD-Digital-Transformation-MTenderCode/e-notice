package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Criterion @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val source: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatesTo: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementGroups: HashSet<RequirementGroup>?
)
