package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class Amendment @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val releaseID: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var amendsReleaseID: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val rationale: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: Set<String>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val changes: List<Change>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?
)