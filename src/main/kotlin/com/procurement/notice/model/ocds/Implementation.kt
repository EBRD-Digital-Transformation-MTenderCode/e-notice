package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

data class Implementation @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val transactions: HashSet<Transaction>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: HashSet<Milestone>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: HashSet<Document>?
)
