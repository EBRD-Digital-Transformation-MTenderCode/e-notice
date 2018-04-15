package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("buyerProcedureReview", "reviewBodyChallenge", "legalProcedures")
data class ReviewProceedings(

        @JsonProperty("buyerProcedureReview")
        @get:JsonProperty("buyerProcedureReview")
        val buyerProcedureReview: Boolean?,

        @JsonProperty("reviewBodyChallenge")
        @get:JsonProperty("reviewBodyChallenge")
        val reviewBodyChallenge: Boolean?,

        @JsonProperty("legalProcedures")
        val legalProcedures: HashSet<LegalProceedings>?
)