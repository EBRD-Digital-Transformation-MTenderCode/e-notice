package com.procurement.notice.infrastructure.dto.entity.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.documents.RecordDocumentReference

data class RecordEvidence(

    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
    @param:JsonProperty("title") @field:JsonProperty("title") val title: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("description") @field:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("relatedDocument") @field:JsonProperty("relatedDocument") val relatedDocument: RecordDocumentReference?

)

