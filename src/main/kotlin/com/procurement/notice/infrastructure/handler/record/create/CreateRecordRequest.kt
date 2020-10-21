package com.procurement.notice.infrastructure.handler.record.create

import com.fasterxml.jackson.annotation.JsonProperty

data class CreateRecordRequest(
    @field:JsonProperty("date") @param:JsonProperty("date") val date: String,
    @field:JsonProperty("data") @param:JsonProperty("data") val data: String
)