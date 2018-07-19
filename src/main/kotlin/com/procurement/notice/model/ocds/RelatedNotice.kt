package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RelatedNotice @JsonCreator constructor(

        val id: String?,

        val scheme: RelatedNoticeScheme?,

        val relationship: Relationship?,

        val objectOfProcurementInPIN: String?,

        val uri: String?
)

