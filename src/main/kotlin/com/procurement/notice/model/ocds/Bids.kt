package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("statistics", "details")
data class Bids(

        @JsonProperty("statistics")
        val statistics: List<BidsStatistic>?,

        @JsonProperty("details")
        val details: List<Bid>?
)