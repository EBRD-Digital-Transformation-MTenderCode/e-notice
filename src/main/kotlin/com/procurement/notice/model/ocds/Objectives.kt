package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Objectives @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val types: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val additionalInformation: String?
)