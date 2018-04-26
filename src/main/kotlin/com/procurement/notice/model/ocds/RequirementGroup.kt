package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "description", "requirements")
data class RequirementGroup(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("requirements")
        val requirements: HashSet<Requirement>?
)