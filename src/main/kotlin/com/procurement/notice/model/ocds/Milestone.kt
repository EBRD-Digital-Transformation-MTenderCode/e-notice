package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        @JsonSerialize(using = JsonDateSerializer::class)
        val dueDate: LocalDateTime?,

        @JsonProperty("dateMet")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val dateMet: LocalDateTime?,

        @JsonProperty("dateModified")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
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