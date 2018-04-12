package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

@JsonPropertyOrder("awardPeriod", "awards", "lots", "bids")
data class AwardPeriodEndDto(

        @JsonProperty("awardPeriod")
        val awardPeriod: Period,

        @JsonProperty("awards")
        val awards: List<Award>,

        @JsonProperty("lots")
        val lots: List<Lot>,

        @JsonProperty("bids")
        val bids: List<Bid>
)
