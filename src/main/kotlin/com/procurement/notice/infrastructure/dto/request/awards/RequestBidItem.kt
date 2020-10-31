package com.procurement.notice.infrastructure.dto.request.awards

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.tender.RequestUnit

data class RequestBidItem(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: RequestUnit
)
