package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ReviewProceedings @JsonCreator constructor(

        @get:JsonProperty("buyerProcedureReview")
        val buyerProcedureReview: Boolean?,

        @get:JsonProperty("reviewBodyChallenge")
        val reviewBodyChallenge: Boolean?,

        val legalProcedures: HashSet<LegalProceedings>?
)