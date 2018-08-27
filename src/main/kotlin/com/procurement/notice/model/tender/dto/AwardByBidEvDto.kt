package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot

@JsonInclude(JsonInclude.Include.NON_NULL)
data class AwardByBidEvDto @JsonCreator constructor(

        val award: Award,

        val nextAwardForUpdate: Award?,

        val bid: Bid,

        val lot: Lot?
)
