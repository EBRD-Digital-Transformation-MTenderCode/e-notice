package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class RelatedProcess @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relationship: List<RelatedProcessType>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: RelatedProcessScheme?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val identifier: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)