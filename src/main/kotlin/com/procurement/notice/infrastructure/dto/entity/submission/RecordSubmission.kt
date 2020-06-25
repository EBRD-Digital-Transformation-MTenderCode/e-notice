package com.procurement.notice.infrastructure.dto.entity.submission

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordSubmission(
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<RecordSubmissionDetail> = emptyList()
)