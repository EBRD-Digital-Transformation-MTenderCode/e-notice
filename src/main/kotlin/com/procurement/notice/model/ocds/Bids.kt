package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("statistics", "details")
data class Bids(

        @JsonProperty("statistics")
        val statistics: List<BidsStatistic>?,

        @JsonProperty("details")
        val details: List<Bid>?
)