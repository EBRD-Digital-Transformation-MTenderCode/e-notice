package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonPropertyOrder("transactions", "milestones", "documents")
data class Implementation(

        @JsonProperty("transactions")
        val transactions: HashSet<Transaction>?,

        @JsonProperty("milestones")
        val milestones: HashSet<Milestone>?,

        @JsonProperty("documents")
        val documents: HashSet<Document>?
)
