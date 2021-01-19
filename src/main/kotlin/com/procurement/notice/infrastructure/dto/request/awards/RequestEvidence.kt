package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocumentReference

data class RequestEvidence(

    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
    @param:JsonProperty("title") @field:JsonProperty("title") val title: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("description") @field:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("relatedDocument") @field:JsonProperty("relatedDocument") val relatedDocument: RequestDocumentReference?

)

