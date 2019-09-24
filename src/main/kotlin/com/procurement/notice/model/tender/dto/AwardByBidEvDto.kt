package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot

data class AwardByBidEvDto @JsonCreator constructor(

    val award: Award,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val nextAwardForUpdate: Award?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val consideredBid: Bid?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val bid: Bid?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val lot: Lot?
)
