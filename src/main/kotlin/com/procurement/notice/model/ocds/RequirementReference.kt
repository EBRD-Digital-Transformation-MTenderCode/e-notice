package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "title")
data class RequirementReference(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?
)