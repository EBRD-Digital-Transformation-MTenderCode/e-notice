package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import java.util.*

@JsonPropertyOrder("awards", "bids", "lots")
data class AwardByBidEvDto(

        @JsonProperty("awards")
        val awards: HashSet<Award>,

        @JsonProperty("bids")
        val bids: HashSet<Bid>,

        @JsonProperty("lots")
        val lots: HashSet<Lot>?
)
