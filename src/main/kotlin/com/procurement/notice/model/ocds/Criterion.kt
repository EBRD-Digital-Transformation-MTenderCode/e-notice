package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Criterion @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val source: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatesTo: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedItem: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementGroups: HashSet<RequirementGroup>?
)