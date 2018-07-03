package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

data class Bids @JsonCreator constructor(

        val statistics: HashSet<BidsStatistic>?,

        val details: HashSet<Bid>?
)