package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

data class Bids @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val statistics: HashSet<BidsStatistic>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val details: HashSet<Bid>?
)