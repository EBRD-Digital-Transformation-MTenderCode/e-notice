package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class RelatedNotice @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val scheme: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relationship: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val objectOfProcurementInPIN: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)

