package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Document @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val documentType: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var url: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var datePublished: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dateModified: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val format: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val language: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedConfirmations: List<String>?
)
