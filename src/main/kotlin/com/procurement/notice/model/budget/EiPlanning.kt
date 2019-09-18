package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class EiPlanning @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: EiBudget?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?
)
