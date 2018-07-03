package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class RelatedNotice @JsonCreator constructor(

        val id: String?,

        val scheme: RelatedNoticeScheme?,

        val relationship: Relationship?,

        val objectOfProcurementInPIN: String?,

        val uri: String?
)

