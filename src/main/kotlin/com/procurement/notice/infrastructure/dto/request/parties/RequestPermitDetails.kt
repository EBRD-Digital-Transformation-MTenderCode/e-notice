package com.procurement.notice.infrastructure.dto.request.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestIssue
import com.procurement.notice.infrastructure.dto.request.RequestPeriod

data class RequestPermitDetails(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("issuedBy") @param:JsonProperty("issuedBy") val issuedBy: RequestIssue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("issuedThought") @param:JsonProperty("issuedThought") val issuedThought: RequestIssue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("validityPeriod") @param:JsonProperty("validityPeriod") val validityPeriod: RequestPeriod?
)