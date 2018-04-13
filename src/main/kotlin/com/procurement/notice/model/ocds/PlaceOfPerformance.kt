package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("address", "description", "NUTScode")
data class PlaceOfPerformance(

        @JsonProperty("address")
        val address: Address?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("NUTScode")
        val nutScode: String?
)