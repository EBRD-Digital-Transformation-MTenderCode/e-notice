package com.procurement.notice.infrastructure.dto.entity.awards

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.tender.RecordUnit

data class RecordBidItem(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: RecordUnit
)
