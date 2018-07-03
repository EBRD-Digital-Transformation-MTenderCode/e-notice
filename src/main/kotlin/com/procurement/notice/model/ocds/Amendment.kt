package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.time.LocalDateTime

data class Amendment @JsonCreator constructor(


        val id: String?,

        val date: LocalDateTime?,

        val releaseID: String?,

        val description: String?,

        val amendsReleaseID: String?,

        val rationale: String?,

        val changes: List<Change>?
)
