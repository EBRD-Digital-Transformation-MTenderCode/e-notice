package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid

data class AwardByBidDto @JsonCreator constructor(

        val award: Award,

        val bid: Bid
)
