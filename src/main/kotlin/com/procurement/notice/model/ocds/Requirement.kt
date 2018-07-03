package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Requirement @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val dataType: DataType?,

        val pattern: String?,

        val expectedValue: String?,

        val minValue: Double?,

        val maxValue: Double?,

        val period: Period?
)

