package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("buyerProcedureReview", "reviewBodyChallenge", "legalProcedures")
data class ReviewProceedings(

        @JsonProperty("buyerProcedureReview")
        val buyerProcedureReview: Boolean?,

        @JsonProperty("reviewBodyChallenge")
        val reviewBodyChallenge: Boolean?,

        @JsonProperty("legalProcedures")
        val legalProcedures: HashSet<LegalProceedings>?
)