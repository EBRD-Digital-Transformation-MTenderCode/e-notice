package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class MsPlanning @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: MsBudget?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?

)
