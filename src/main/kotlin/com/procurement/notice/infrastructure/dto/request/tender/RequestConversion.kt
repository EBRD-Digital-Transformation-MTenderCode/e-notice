package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestConversion(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("relatesTo") @param:JsonProperty("relatesTo") val relatesTo: String,
    @field:JsonProperty("relatedItem") @param:JsonProperty("relatedItem") val relatedItem: String,
    @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("coefficients") @param:JsonProperty("coefficients") val coefficients: List<RequestCoefficient> = emptyList()
)