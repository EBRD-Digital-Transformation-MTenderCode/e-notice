package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BidsStatistic @JsonCreator constructor(

        val id: String?,

        val measure: Measure?,

        val date: LocalDateTime?,

        val value: Double?,

        val notes: String?,

        val relatedLot: String?
)
