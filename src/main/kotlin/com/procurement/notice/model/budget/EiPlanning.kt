package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class EiPlanning @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: EiBudget?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?
)
