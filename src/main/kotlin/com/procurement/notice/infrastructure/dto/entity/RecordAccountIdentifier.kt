package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordAccountIdentifier(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String?
)