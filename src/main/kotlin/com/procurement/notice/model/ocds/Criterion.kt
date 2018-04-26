package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "title", "description", "source", "relatesTo", "relatedItem", "requirementGroups")
data class Criterion(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("source")
        val source: Source?,

        @JsonProperty("relatesTo")
        val relatesTo: RelatesTo?,

        @JsonProperty("relatedItem")
        val relatedItem: String?,

        @JsonProperty("requirementGroups")
        val requirementGroups: HashSet<RequirementGroup>?
)