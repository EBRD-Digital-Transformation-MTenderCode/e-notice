package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.point.databinding.JsonDateDeserializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "id",
        "title",
        "description",
        "status",
        "statusDetails",
        "date",
        "value",
        "suppliers",
        "items",
        "contractPeriod",
        "documents",
        "amendments",
        "amendment",
        "relatedLots",
        "requirementResponses",
        "reviewProceedings")
data class Award(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        var description: String?,

        @JsonProperty("status")
        val status: AwardStatus?,

        @JsonProperty("statusDetails")
        var statusDetails: AwardStatus?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        var date: LocalDateTime?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("suppliers")
        val suppliers: HashSet<OrganizationReference>?,

        @JsonProperty("items")
        val items: HashSet<Item>?,

        @JsonProperty("contractPeriod")
        val contractPeriod: Period?,

        @JsonProperty("documents")
        var documents: HashSet<Document>?,

        @JsonProperty("amendments")
        val amendments: List<Amendment>?,

        @JsonProperty("amendment")
        val amendment: Amendment?,

        @JsonProperty("relatedLots")
        val relatedLots: List<String>?,

        @JsonProperty("requirementResponses")
        val requirementResponses: HashSet<RequirementResponse>?,

        @JsonProperty("reviewProceedings")
        val reviewProceedings: ReviewProceedings?,

        @JsonProperty("relatedBid")
        val relatedBid: String?
)
