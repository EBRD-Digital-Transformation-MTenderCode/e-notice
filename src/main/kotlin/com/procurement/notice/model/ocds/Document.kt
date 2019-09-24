package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Document @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val documentType: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var url: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var datePublished: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dateModified: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val format: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val language: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedConfirmations: List<String>?
)
