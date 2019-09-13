package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class EuropeanUnionFunding @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val projectIdentifier: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val projectName: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val uri: String?
)
