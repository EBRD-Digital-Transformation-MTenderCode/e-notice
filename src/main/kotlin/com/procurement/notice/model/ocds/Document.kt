package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Document @JsonCreator constructor(

        val id: String?,

        val documentType: String?,

        val title: String?,

        val description: String?,

        var url: String?,

        var datePublished: LocalDateTime?,

        val dateModified: LocalDateTime?,

        val format: String?,

        val language: String?,

        val relatedLots: List<String>?
)
