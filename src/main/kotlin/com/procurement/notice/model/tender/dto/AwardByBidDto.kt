package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid

@JsonPropertyOrder("award", "bid")
data class AwardByBidDto(

        @JsonProperty("award")
        val award: Award,

        @JsonProperty("bid")
        val bid: Bid
)
