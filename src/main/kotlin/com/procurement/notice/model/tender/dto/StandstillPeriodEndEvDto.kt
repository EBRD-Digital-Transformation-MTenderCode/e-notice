package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

@JsonPropertyOrder("standstillPeriod", "contracts")
data class StandstillPeriodEndEvDto(

        @JsonProperty("standstillPeriod")
        val standstillPeriod: Period,

        @JsonProperty("cans")
        val cans: HashSet<Can>
)
