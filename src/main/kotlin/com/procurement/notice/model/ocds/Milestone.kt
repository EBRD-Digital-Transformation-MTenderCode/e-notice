package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.point.databinding.JsonDateDeserializer
import java.time.LocalDateTime
import java.util.*

@JsonPropertyOrder("id", "title", "type", "description", "code", "dueDate", "dateMet", "dateModified", "status", "documents", "relatedLots", "relatedParties", "additionalInformation")
data class Milestone(

        @JsonProperty("id")
        private val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("type")
        val type: MilestoneType?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("code")
        val code: String?,

        @JsonProperty("dueDate")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val dueDate: LocalDateTime?,

        @JsonProperty("dateMet")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val dateMet: LocalDateTime?,

        @JsonProperty("dateModified")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val dateModified: LocalDateTime?,

        @JsonProperty("status")
        val status: MilestoneStatus?,

        @JsonProperty("documents")
        val documents: HashSet<Document>?,

        @JsonProperty("relatedLots")
        val relatedLots: List<String>?,

        @JsonProperty("relatedParties")
        val relatedParties: List<OrganizationReference>?,

        @JsonProperty("additionalInformation")
        val additionalInformation: String?
)