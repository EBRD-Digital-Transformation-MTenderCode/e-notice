package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document

data class UpdateCanDocumentsDto @JsonCreator constructor(

    val contract: UpdateDocumentContract
)

data class UpdateDocumentContract @JsonCreator constructor(

    val id: String,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>
)