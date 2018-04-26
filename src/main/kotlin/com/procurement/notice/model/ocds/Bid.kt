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
@JsonPropertyOrder("id", "date", "status", "statusDetails", "tenderers", "value", "documents", "relatedLots", "requirementResponses")
data class Bid(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        var date: LocalDateTime?,

        @JsonProperty("status")
        var status: BidStatus?,

        @JsonProperty("statusDetails")
        var statusDetails: BidStatusDetails?,

        @JsonProperty("tenderers")
        val tenderers: List<OrganizationReference>?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("documents")
        var documents: HashSet<Document>?,

        @JsonProperty("relatedLots")
        val relatedLots: List<String>?,

        @JsonProperty("requirementResponses")
        val requirementResponses: HashSet<RequirementResponse>?
)


