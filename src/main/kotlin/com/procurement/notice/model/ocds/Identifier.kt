package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("scheme", "id", "legalName", "uri")
data class Identifier(

        @JsonProperty("scheme")
        val scheme: String?,

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("legalName")
        val legalName: String?,

        @JsonProperty("uri")
        val uri: String?
)
