package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document
import java.time.LocalDateTime

data class Can @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val token: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val awardId: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val lotId: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: List<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var amendment: Amendment?
)

data class Amendment @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<DocumentAmendment>?
)

data class DocumentAmendment @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var documentType: Document?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?
)
