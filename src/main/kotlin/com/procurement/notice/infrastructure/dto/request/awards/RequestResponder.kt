package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.award.ResponderId

data class RequestResponder(
    @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: Identifier,
    @param:JsonProperty("name") @field:JsonProperty("name") val name: String
){
    data class Identifier(
        @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
        @param:JsonProperty("id") @field:JsonProperty("id") val id: ResponderId
    )
}