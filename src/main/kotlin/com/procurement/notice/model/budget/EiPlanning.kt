package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator

data class EiPlanning @JsonCreator constructor(

        val budget: EiBudget?,

        val rationale: String?
)
