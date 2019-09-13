package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document
import java.time.LocalDateTime

data class Can @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val token: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val awardId: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val lotId: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var status: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var amendment: Amendment?
)

data class Amendment @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<DocumentAmendment>?
)

data class DocumentAmendment @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var documentType: Document?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?
)