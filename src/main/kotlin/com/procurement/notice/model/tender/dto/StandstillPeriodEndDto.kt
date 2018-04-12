package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

@JsonPropertyOrder("standstillPeriod", "lots")
data class StandstillPeriodEndDto(

        @JsonProperty("standstillPeriod")
        val standstillPeriod: Period,

        @JsonProperty("lots")
        val lots: List<Lot>
)
