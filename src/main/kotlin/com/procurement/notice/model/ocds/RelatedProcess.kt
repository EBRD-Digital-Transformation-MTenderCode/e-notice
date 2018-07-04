package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RelatedProcess @JsonCreator constructor(

        val id: String?,

        val relationship: List<RelatedProcessType>?,

        val scheme: RelatedProcessScheme?,

        val identifier: String?,

        val uri: String?
)