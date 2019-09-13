package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Period @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val startDate: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val endDate: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val maxExtentDate: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val durationInDays: Int?
)