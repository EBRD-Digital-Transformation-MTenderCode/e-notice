package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("property", "former_value")
data class Change(

        @JsonProperty("property")
        val property: String?,

        @JsonProperty("former_value")
        val formerValue: Any?
)
