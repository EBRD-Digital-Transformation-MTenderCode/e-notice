package com.procurement.notice.infrastructure.dto.request.submissions

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestSubmission(
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<RequestSubmissionDetail> = emptyList()
)