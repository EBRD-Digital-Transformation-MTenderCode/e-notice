package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty

data class BidItem(
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: Unit
)