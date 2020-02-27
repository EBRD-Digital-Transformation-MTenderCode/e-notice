package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocumentBF

data class RequestBusinessFunction (

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonProperty("type") @param:JsonProperty("type") val type: String,

    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String,

    @field:JsonProperty("period") @param:JsonProperty("period") val period: RequestPeriod,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<RequestDocumentBF> = emptyList()
)