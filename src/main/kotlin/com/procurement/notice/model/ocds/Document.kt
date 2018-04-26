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
@JsonPropertyOrder("id", "documentType", "title", "description", "url", "datePublished", "dateModified", "format", "language", "relatedLots")
data class Document(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("documentType")
        val documentType: DocumentType?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("url")
        var url: String?,

        @JsonProperty("datePublished")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        var datePublished: LocalDateTime?,

        @JsonProperty("dateModified")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val dateModified: LocalDateTime?,

        @JsonProperty("format")
        val format: String?,

        @JsonProperty("language")
        val language: String? = "en",

        @JsonProperty("relatedLots")
        val relatedLots: List<String>?
)
