package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ReviewProceedings @JsonCreator constructor(

    @get:JsonProperty("buyerProcedureReview")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val buyerProcedureReview: Boolean?,

    @get:JsonProperty("reviewBodyChallenge")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val reviewBodyChallenge: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val legalProcedures: HashSet<LegalProceedings>?
)
