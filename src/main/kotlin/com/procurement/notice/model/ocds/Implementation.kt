package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

data class Implementation @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val transactions: HashSet<Transaction>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: HashSet<Milestone>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: HashSet<Document>?
)
