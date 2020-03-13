package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

data class CancellationStandstillPeriodDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val amendments: List<Amendment>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val standstillPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: List<Award> = emptyList()
)
