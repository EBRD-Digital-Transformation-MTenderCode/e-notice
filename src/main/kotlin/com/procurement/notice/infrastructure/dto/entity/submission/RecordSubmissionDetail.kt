package com.procurement.notice.infrastructure.dto.entity.submission

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import com.procurement.notice.infrastructure.dto.entity.awards.RecordRequirementResponse
import com.procurement.notice.infrastructure.dto.entity.documents.RecordDocument
import java.time.LocalDateTime

data class RecordSubmissionDetail(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonDeserialize(using = JsonDateTimeDeserializer::class)
    @JsonSerialize(using = JsonDateTimeSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") val status: SubmissionStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("requirementResponses") @param:JsonProperty("requirementResponses") val requirementResponses: List<RecordRequirementResponse> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("candidates") @param:JsonProperty("candidates") val candidates: List<RecordCandidate> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<RecordDocument> = emptyList()

)