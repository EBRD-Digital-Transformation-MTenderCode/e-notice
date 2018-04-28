package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import java.util.*

@JsonPropertyOrder("awards", "bids", "lots")
data class AwardByBidEvDto(

        @JsonProperty("award")
        val award: Award,

        @JsonProperty("nextAward")
        val nextAward: Award?,

        @JsonProperty("bid")
        val bid: Bid,

        @JsonProperty("lot")
        val lot: Lot?
)
