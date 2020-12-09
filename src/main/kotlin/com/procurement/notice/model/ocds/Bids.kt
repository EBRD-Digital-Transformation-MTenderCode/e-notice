package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Bids @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val statistics: List<BidsStatistic>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val details: List<Bid>?
)
