package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestRelatedOrganization(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
)