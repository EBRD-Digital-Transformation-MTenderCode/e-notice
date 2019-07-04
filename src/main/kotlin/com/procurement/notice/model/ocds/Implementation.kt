package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Implementation @JsonCreator constructor(

        val transactions: HashSet<Transaction>?,

        val milestones: HashSet<Milestone>?,

        val documents: HashSet<Document>?
)
