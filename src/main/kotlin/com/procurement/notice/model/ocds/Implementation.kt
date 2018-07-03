package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.util.*

data class Implementation @JsonCreator constructor(

        val transactions: HashSet<Transaction>?,

        val milestones: HashSet<Milestone>?,

        val documents: HashSet<Document>?
)
