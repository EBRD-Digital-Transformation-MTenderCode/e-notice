package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("address", "description", "NUTScode")
data class PlaceOfPerformance(

        @JsonProperty("address")
        val address: Address?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("NUTScode")
        val nutScode: String?
)