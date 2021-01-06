package com.procurement.notice.infrastructure.dto.entity.tender.criteria

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordEligibleEvidence(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("type") @param:JsonProperty("type") val type: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedDocument") @param:JsonProperty("relatedDocument") val relatedDocument: String?
)