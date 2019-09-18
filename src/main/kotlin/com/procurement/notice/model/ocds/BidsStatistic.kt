package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class BidsStatistic @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val measure: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Double?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val notes: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?
)
