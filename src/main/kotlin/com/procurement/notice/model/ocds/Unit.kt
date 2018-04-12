package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "value", "scheme", "id", "uri")
data class Unit(

        @JsonProperty("name")
        val name: String?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("scheme")
        val scheme: UnitScheme?,

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("uri")
        val uri: String?
)
