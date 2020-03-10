package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestAccountIdentifier(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String
)