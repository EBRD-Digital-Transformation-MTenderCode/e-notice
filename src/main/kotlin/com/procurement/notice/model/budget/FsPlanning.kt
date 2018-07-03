package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator

data class FsPlanning @JsonCreator constructor(

        val budget: FsBudget?,

        val rationale: String?
)
