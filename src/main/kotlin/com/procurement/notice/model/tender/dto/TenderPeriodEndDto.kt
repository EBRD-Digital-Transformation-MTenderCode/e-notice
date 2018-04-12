package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.*

@JsonPropertyOrder("tenderPeriod", "awardPeriod", "awards", "lots", "tenderers", "bids")
data class TenderPeriodEndDto(

        @JsonProperty("tenderPeriod")
        val tenderPeriod: Period,

        @JsonProperty("awardPeriod")
        val awardPeriod: Period,

        @JsonProperty("awards")
        val awards: List<Award>,

        @JsonProperty("lots")
        val lots: List<Lot>,

        @JsonProperty("tenderers")
        val tenderers: List<OrganizationReference>,

        @JsonProperty("bids")
        val bids: List<Bid>
)
