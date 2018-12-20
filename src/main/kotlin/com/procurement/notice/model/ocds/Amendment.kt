package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Amendment @JsonCreator constructor(

        var id: String?,

        var date: LocalDateTime?,

        val releaseID: String?,

        val description: String?,

        var amendsReleaseID: String?,

        val rationale: String?,

        val relatedLots: Set<String>?,

        val changes: List<Change>?,

        val documents: List<Document>?
)