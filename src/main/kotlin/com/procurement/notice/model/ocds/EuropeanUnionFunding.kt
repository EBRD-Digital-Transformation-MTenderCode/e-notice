package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("projectIdentifier", "projectName", "uri")
data class EuropeanUnionFunding(

        @JsonProperty("projectIdentifier")
        val projectIdentifier: String?,

        @JsonProperty("projectName")
        val projectName: String?,

        @JsonProperty("uri")
        val uri: String?
)
