package com.procurement.notice.model.tender.enquiry

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "date", "author", "title", "description", "answer", "dateAnswered", "relatedItem", "relatedLot")
data class RecordEnquiry(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val date: LocalDateTime?,

        @JsonProperty("author")
        val author: OrganizationReference?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("answer")
        var answer: String?,

        @JsonProperty("dateAnswered")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        var dateAnswered: LocalDateTime?,

        @JsonProperty("relatedItem")
        val relatedItem: String?,

        @JsonProperty("relatedLot")
        val relatedLot: String?
)
