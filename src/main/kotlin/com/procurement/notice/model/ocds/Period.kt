package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class Period @JsonCreator constructor(

        val startDate: LocalDateTime?,

        val endDate: LocalDateTime?,

        val maxExtentDate: LocalDateTime?,

        val durationInDays: Int?
)