package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.parties.PersonId

data class RequestResponder(
    @param:JsonProperty("id") @field:JsonProperty("id") val id: PersonId,
    @param:JsonProperty("name") @field:JsonProperty("name") val name: String
)
