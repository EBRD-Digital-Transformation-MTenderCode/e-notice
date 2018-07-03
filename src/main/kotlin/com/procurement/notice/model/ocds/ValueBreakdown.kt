package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class ValueBreakdown @JsonCreator constructor(

        val id: String?,

        val type: List<ValueBreakdownType>?,

        val description: String?,

        val amount: Value?,

        val estimationMethod: Value?
)
