package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Person @JsonCreator constructor(

        var title: String,

        var name: String,

        val identifier: Identifier,

        var businessFunctions: List<BusinessFunction>
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class BusinessFunction @JsonCreator constructor(

        val id: String,

        var type: String,

        var jobTitle: String,

        var period: Period,

        var documents: List<DocumentBF>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DocumentBF @JsonCreator constructor(

        val id: String,

        val documentType: String,

        var title: String?,

        var description: String?,

        var url: String?,

        var datePublished: LocalDateTime?,

        var dateModified: LocalDateTime?
)