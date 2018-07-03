package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Change @JsonCreator constructor(

        val property: String?,

        val formerValue: Any?
)
