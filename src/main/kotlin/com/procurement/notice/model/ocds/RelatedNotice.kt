package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class RelatedNotice @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relationship: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val objectOfProcurementInPIN: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
