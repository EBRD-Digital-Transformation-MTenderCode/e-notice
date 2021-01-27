package com.procurement.notice.infrastructure.dto.request.documents

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestDocumentReference(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("id") @field:JsonProperty("id") val id: String?
)