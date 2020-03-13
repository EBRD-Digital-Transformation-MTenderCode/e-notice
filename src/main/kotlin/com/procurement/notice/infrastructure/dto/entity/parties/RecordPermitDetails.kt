package com.procurement.notice.infrastructure.dto.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordIssue
import com.procurement.notice.infrastructure.dto.entity.RecordPeriod

data class RecordPermitDetails(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("issuedBy") @param:JsonProperty("issuedBy") val issuedBy: RecordIssue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("issuedThought") @param:JsonProperty("issuedThought") val issuedThought: RecordIssue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("validityPeriod") @param:JsonProperty("validityPeriod") val validityPeriod: RecordPeriod?
)