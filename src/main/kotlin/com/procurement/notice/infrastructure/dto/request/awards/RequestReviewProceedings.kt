package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestLegalProceedings

data class RequestReviewProceedings(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("buyerProcedureReview") @param:JsonProperty("buyerProcedureReview") val buyerProcedureReview: Boolean?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("reviewBodyChallenge") @param:JsonProperty("reviewBodyChallenge") val reviewBodyChallenge: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("legalProcedures") @param:JsonProperty("legalProcedures") val legalProcedures: List<RequestLegalProceedings> = emptyList()
)
