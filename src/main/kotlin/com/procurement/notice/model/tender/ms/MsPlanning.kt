package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class MsPlanning @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: MsBudget?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?

)
