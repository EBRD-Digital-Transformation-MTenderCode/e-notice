package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Planning @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val budget: Budget?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val rationale: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>?
)