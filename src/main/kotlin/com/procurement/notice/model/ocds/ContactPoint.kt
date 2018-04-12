package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("name", "email", "telephone", "faxNumber", "url")
data class ContactPoint(

        @JsonProperty("name")
        val name: String?,

        @JsonProperty("email")
        val email: String?,

        @JsonProperty("telephone")
        val telephone: String?,

        @JsonProperty("faxNumber")
        val faxNumber: String?,

        @JsonProperty("url")
        val url: String?
)