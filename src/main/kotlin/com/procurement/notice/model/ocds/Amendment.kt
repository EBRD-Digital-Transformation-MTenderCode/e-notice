package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "date", "rationale", "description", "amendsReleaseID", "releaseID", "changes")
data class Amendment(


        @JsonProperty("id")
        val id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val date: LocalDateTime?,

        @JsonProperty("releaseID")
        val releaseID: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("amendsReleaseID")
        val amendsReleaseID: String?,

        @JsonProperty("rationale")
        val rationale: String?,

        @JsonProperty("changes")
        val changes: List<Change>?
)
