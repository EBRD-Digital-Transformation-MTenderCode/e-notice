package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class RelatedProcess @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relationship: List<RelatedProcessType>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: RelatedProcessScheme?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val identifier: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
