package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class FsPlanning @JsonCreator constructor(

        val budget: FsBudget?,

        val rationale: String?
)
