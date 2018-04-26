package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("transactions", "milestones", "documents")
data class Implementation(

        @JsonProperty("transactions")
        val transactions: HashSet<Transaction>?,

        @JsonProperty("milestones")
        val milestones: HashSet<Milestone>?,

        @JsonProperty("documents")
        val documents: HashSet<Document>?
)
