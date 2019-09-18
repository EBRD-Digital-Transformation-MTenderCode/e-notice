package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Change @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val property: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val formerValue: Any?
)
