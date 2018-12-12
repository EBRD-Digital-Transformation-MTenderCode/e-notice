package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Document

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateCanDocumentsDto @JsonCreator constructor(

    val contract: UpdateDocumentContract
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateDocumentContract @JsonCreator constructor(

    val id:String,
    val documents: List<Document>
)