package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("types", "additionalInformation")
data class Objectives(

        @JsonProperty("types")
        val types: List<ObjectivesType>?,

        @JsonProperty("additionalInformation")
        val additionalInformation: String?
)