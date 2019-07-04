package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Bids @JsonCreator constructor(

        val statistics: HashSet<BidsStatistic>?,

        val details: HashSet<Bid>?
)