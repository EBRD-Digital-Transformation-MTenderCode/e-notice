package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Change @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val property: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val formerValue: Any?
)
