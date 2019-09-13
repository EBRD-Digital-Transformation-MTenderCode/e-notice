package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Person @JsonCreator constructor(

    var title: String,

    var name: String,

    val identifier: Identifier,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var businessFunctions: List<BusinessFunction>
)

data class BusinessFunction @JsonCreator constructor(

    val id: String,

    var type: String,

    var jobTitle: String,

    var period: Period,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: List<DocumentBF>
)

data class DocumentBF @JsonCreator constructor(

    val id: String,

    val documentType: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var url: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var datePublished: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var dateModified: LocalDateTime?
)