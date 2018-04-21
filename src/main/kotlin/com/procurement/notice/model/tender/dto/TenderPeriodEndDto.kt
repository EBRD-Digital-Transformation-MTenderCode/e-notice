package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.*
import java.util.*

@JsonPropertyOrder("tenderPeriod", "awardPeriod", "awards", "lots", "bids")
data class TenderPeriodEndDto(

        @JsonProperty("tenderPeriod")
        val tenderPeriod: Period,

        @JsonProperty("awardPeriod")
        val awardPeriod: Period,

        @JsonProperty("awards")
        val awards: HashSet<Award>,

        @JsonProperty("lots")
        val lots: HashSet<Lot>,

        @JsonProperty("bids")
        val bids: HashSet<Bid>,

        @JsonProperty("documents")
        val documents: HashSet<Document>
)
