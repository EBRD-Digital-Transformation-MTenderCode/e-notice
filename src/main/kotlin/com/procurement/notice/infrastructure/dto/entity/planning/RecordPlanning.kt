package com.procurement.notice.infrastructure.dto.entity.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordPlanning(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("implementation") @param:JsonProperty("implementation") val implementation: RecordImplementation?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("budget") @param:JsonProperty("budget") val budget: RecordPlanningBudget?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String?
)
