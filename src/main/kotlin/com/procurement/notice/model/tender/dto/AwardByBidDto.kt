package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid


@JsonInclude(JsonInclude.Include.NON_NULL)
data class AwardByBidDto @JsonCreator constructor(

        val award: Award,

        val bid: Bid,

        val consideredBid: Bid?
)
