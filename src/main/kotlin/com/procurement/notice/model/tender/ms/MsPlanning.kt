package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator

data class MsPlanning @JsonCreator constructor(

        val budget: MsBudget?,

        val rationale: String?

)
