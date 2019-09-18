package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Planning @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: Budget?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>?
)
