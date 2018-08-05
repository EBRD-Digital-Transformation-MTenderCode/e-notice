package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ValueBreakdown @JsonCreator constructor(

        val id: String?,

        val type: List<String>?,

        val description: String?,

        val amount: Value?,

        val estimationMethod: Value?
)
