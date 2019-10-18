package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Amendment @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val releaseID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var amendsReleaseID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val changes: List<Change>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?
)
