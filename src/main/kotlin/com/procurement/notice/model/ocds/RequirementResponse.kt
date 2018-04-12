package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "title", "description", "value", "period", "requirement", "relatedTenderer")
data class RequirementResponse(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("value")
        val value: String?,

        @JsonProperty("period")
        val period: Period?,

        @JsonProperty("requirement")
        val requirement: RequirementReference?,

        @JsonProperty("relatedTenderer")
        val relatedTenderer: OrganizationReference?
)