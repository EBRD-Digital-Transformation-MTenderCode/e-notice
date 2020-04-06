package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateRecordRequest(
    @field:JsonProperty("date") @param:JsonProperty("date") val date: String,
    @field:JsonProperty("data") @param:JsonProperty("data") val data: String
)