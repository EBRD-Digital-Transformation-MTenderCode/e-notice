package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class Document @JsonCreator constructor(

        val id: String?,

        val documentType: DocumentType?,

        val title: String?,

        val description: String?,

        var url: String?,

        var datePublished: LocalDateTime?,

        val dateModified: LocalDateTime?,

        val format: String?,

        val language: String? = "en",

        val relatedLots: List<String>?
)
