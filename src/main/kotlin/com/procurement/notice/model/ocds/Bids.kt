package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("statistics", "details")
data class Bids(

        @JsonProperty("statistics")
        val statistics: HashSet<BidsStatistic>?,

        @JsonProperty("details")
        val details: HashSet<Bid>?
)