package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class BidsStatistic @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val measure: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Double?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val notes: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?
)
