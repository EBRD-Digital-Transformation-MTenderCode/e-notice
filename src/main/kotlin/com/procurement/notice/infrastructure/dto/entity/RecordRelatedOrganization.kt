package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonProperty

data class RecordRelatedOrganization(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("name") @param:JsonProperty("name") val name: String
)