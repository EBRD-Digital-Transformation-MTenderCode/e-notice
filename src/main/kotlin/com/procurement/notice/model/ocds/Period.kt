package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Period @JsonCreator constructor(

        val startDate: LocalDateTime?,

        val endDate: LocalDateTime?,

        val maxExtentDate: LocalDateTime?,

        val durationInDays: Int?
)