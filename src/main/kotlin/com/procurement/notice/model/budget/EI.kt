package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.model.ocds.*
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "ocid",
        "id",
        "date",
        "tag",
        "initiationType",
        "title",
        "description",
        "language",
        "planning",
        "tender",
        "parties",
        "buyer",
        "relatedProcesses")
data class EI(

        @JsonProperty("ocid")
        val ocid: String,

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        var date: LocalDateTime?,

        @JsonProperty("tag")
        var tag: List<Tag>?,

        @JsonProperty("initiationType")
        var initiationType: InitiationType?,

        @JsonProperty("title")
        var title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("language")
        val language: String?,

        @JsonProperty("tender")
        var tender: Tender?,

        @JsonProperty("buyer")
        val buyer: OrganizationReference?,

        @JsonProperty("parties")
        var parties: HashSet<Organization>?,

        @JsonProperty("planning")
        var planning: EiPlanning?,

        @JsonProperty("relatedProcesses")
        var relatedProcesses: HashSet<RelatedProcess>?
)
