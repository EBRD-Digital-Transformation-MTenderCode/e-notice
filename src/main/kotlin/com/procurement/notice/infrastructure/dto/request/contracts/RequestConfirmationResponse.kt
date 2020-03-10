package com.procurement.notice.infrastructure.dto.request.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestConfirmationResponseValue

data class RequestConfirmationResponse(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: RequestConfirmationResponseValue?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("request") @param:JsonProperty("request") val request: String?
)
