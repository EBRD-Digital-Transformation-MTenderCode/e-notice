package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Can @JsonCreator constructor(

        val id: String?,

        val token: String?,

        var date: LocalDateTime?,

        val awardId: String?,

        var status: String?,

        var statusDetails: String?,

        var documents: HashSet<Document>?,

        var amendment: Amendment?
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class Amendment @JsonCreator constructor(

        val rationale: String?,

        val description: String?,

        val documents: List<DocumentAmendment>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class DocumentAmendment @JsonCreator constructor(

        val id: String?,

        var documentType: Document?,

        var title: String?,

        var description: String?
)