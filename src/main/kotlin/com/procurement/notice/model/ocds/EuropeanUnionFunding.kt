package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EuropeanUnionFunding @JsonCreator constructor(

        val projectIdentifier: String?,

        val projectName: String?,

        val uri: String?
)
