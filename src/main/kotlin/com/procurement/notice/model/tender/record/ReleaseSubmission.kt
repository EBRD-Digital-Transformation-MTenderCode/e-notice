package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ReleaseSubmission(
    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<ReleaseSubmissionDetail> = emptyList()
)