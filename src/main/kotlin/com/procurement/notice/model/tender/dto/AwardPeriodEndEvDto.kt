package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.*
import java.util.*

@JsonPropertyOrder("awardPeriod", "awards", "lots", "bids")
data class AwardPeriodEndEvDto(

        @JsonProperty("awardPeriod")
        val awardPeriod: Period,

        @JsonProperty("lots")
        val lots: HashSet<Lot>,


        @JsonProperty("bids")
        val bids: HashSet<Bid>,

        @JsonProperty("awards")
        val awards: HashSet<Award>,

        @JsonProperty("documents")
        val documents: HashSet<Document>,

        @JsonProperty("cans")
        val cans: Can,

        @JsonProperty("contracts")
        val contracts: HashSet<Contract>
)
