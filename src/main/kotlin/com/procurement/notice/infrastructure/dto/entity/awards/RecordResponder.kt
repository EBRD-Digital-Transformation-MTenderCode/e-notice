package com.procurement.notice.infrastructure.dto.entity.awards

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.award.ResponderId

data class RecordResponder(
    @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: Identifier,
    @param:JsonProperty("name") @field:JsonProperty("name") val name: String
){
    data class Identifier(
        @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
        @param:JsonProperty("id") @field:JsonProperty("id") val id: ResponderId
    )
}