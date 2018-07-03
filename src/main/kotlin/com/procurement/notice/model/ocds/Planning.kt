package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class Planning @JsonCreator constructor(

        val budget: Budget?,

        val rationale: String?,

        val documents: List<Document>?,

        val milestones: List<Milestone>?
)