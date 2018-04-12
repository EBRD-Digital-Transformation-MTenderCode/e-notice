package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("maximumLotsBidPerSupplier", "maximumLotsAwardedPerSupplier")
data class LotDetails(

        @JsonProperty("maximumLotsBidPerSupplier")
        val maximumLotsBidPerSupplier: Int?,

        @JsonProperty("maximumLotsAwardedPerSupplier")
        val maximumLotsAwardedPerSupplier: Int?
)