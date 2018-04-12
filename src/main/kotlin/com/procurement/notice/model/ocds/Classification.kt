package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("scheme", "id", "description", "uri")
data class Classification(

        @JsonProperty("scheme")
        val scheme: ClassificationScheme?,

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("uri")
        val uri: String?
)
