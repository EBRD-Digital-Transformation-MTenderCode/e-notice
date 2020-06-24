package com.procurement.notice.infrastructure.dto.entity.awards

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordBusinessFunction
import com.procurement.notice.infrastructure.dto.entity.RecordIdentifier
import com.procurement.notice.infrastructure.dto.entity.parties.PersonId

data class RecordResponder(
    @param:JsonProperty("id") @field:JsonProperty("id") val id: PersonId,
    @param:JsonProperty("name") @field:JsonProperty("name") val name: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("title") @field:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: RecordIdentifier?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @param:JsonProperty("businessFunctions") @field:JsonProperty("businessFunctions") val businessFunctions: List<RecordBusinessFunction> = emptyList()
)
