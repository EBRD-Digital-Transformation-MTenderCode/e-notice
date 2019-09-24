package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Requirement @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dataType: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val pattern: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val expectedValue: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val minValue: Double?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val maxValue: Double?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?
)
