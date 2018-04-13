package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
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
