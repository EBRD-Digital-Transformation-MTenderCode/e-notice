package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RelatedNotice @JsonCreator constructor(

        val id: String?,

        val scheme: String?,

        val relationship: String?,

        val objectOfProcurementInPIN: String?,

        val uri: String?
)

