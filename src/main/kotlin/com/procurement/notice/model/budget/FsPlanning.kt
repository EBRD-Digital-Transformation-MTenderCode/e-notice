package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class FsPlanning @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: FsBudget?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?
)
