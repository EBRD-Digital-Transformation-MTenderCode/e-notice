package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.parties.PersonId
import com.procurement.notice.infrastructure.dto.request.RequestBusinessFunction
import com.procurement.notice.infrastructure.dto.request.RequestIdentifier

data class RequestResponder(
    @param:JsonProperty("id") @field:JsonProperty("id") val id: PersonId,
    @param:JsonProperty("name") @field:JsonProperty("name") val name: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("title") @field:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: RequestIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @param:JsonProperty("businessFunctions") @field:JsonProperty("businessFunctions") val businessFunctions: List<RequestBusinessFunction> = emptyList()
)
