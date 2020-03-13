package com.procurement.notice.infrastructure.dto.entity.parties

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordBusinessFunction
import com.procurement.notice.infrastructure.dto.entity.RecordIdentifier

data class RecordPerson (

    @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

    @field:JsonProperty("name") @param:JsonProperty("name") val name: String,

    @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: RecordIdentifier,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("businessFunctions") @param:JsonProperty("businessFunctions") val businessFunctions: List<RecordBusinessFunction>
)
