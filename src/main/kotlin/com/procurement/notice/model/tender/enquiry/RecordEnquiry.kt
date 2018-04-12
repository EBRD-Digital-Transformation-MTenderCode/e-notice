package com.procurement.notice.model.tender.enquiry

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.point.databinding.JsonDateDeserializer
import java.time.LocalDateTime

@JsonPropertyOrder("id", "date", "author", "title", "description", "answer", "dateAnswered", "relatedItem", "relatedLot")
data class RecordEnquiry(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val date: LocalDateTime?,

        @JsonProperty("author")
        val author: EnquiryAuthor?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("answer")
        var answer: String?,

        @JsonProperty("dateAnswered")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        val dateAnswered: LocalDateTime?,

        @JsonProperty("relatedItem")
        val relatedItem: String?,

        @JsonProperty("relatedLot")
        val relatedLot: String?
)
