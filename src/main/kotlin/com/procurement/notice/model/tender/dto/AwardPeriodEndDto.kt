package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period
import java.util.*

data class AwardPeriodEndDto @JsonCreator constructor(

        val awardPeriod: Period,

        val awards: HashSet<Award>,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>
)
