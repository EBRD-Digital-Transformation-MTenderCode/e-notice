package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class BidsStatistic @JsonCreator constructor(

        val id: String?,

        val measure: Measure?,

        val date: LocalDateTime?,

        val value: Double?,

        val notes: String?,

        val relatedLot: String?
)
