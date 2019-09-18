package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period
import java.util.*

data class AwardPeriodEndDto @JsonCreator constructor(

    val awardPeriod: Period,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>
)
