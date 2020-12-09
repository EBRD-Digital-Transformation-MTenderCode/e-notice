package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class Implementation @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val transactions: List<Transaction>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?
)
