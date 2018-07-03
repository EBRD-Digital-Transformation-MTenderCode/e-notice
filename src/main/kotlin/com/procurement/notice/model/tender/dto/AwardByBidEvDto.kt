package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot

data class AwardByBidEvDto @JsonCreator constructor(

        val award: Award,

        val nextAward: Award?,

        val bid: Bid,

        val lot: Lot?
)
