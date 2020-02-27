package com.procurement.notice.infrastructure.dto.request.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestPlanning(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("implementation") @param:JsonProperty("implementation") val implementation: RequestImplementation?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("budget") @param:JsonProperty("budget") val budget: RequestPlanningBudget?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String?
)
