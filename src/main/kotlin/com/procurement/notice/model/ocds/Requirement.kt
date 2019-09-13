package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Requirement @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dataType: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val pattern: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val expectedValue: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val minValue: Double?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val maxValue: Double?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?
)

