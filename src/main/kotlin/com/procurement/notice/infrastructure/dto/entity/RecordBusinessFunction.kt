package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.documents.RecordDocumentBF

data class RecordBusinessFunction (

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonProperty("type") @param:JsonProperty("type") val type: String,

    @field:JsonProperty("jobTitle") @param:JsonProperty("jobTitle") val jobTitle: String,

    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<RecordDocumentBF> = emptyList()
)