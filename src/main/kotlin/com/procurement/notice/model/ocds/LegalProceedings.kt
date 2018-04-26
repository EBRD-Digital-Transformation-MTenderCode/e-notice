package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "title", "uri")
data class LegalProceedings(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("uri")
        val uri: String?
)
