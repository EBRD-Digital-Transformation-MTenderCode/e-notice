package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "relationship", "scheme", "identifier", "uri")
data class RelatedProcess(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("relationship")
        val relationship: List<RelatedProcessType>?,

        @JsonProperty("scheme")
        val scheme: RelatedProcessScheme?,

        @JsonProperty("identifier")
        val identifier: String?,

        @JsonProperty("uri")
        val uri: String?
)