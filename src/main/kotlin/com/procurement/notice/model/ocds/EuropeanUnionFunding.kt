package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class EuropeanUnionFunding @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val projectIdentifier: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val projectName: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
