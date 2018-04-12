package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("types", "additionalInformation")
data class Objectives(

        @JsonProperty("types")
        val types: List<ObjectivesType>?,

        @JsonProperty("additionalInformation")
        val additionalInformation: String?
)