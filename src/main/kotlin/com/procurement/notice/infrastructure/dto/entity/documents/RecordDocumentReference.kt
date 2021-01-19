package com.procurement.notice.infrastructure.dto.entity.documents

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordDocumentReference(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("id") @field:JsonProperty("id") val id: String?
)