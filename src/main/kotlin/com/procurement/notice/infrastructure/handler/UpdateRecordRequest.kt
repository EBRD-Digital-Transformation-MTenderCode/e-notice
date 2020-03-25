package com.procurement.notice.infrastructure.handler

import com.fasterxml.jackson.annotation.JsonProperty

data class UpdateRecordRequest(
    @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: String,
    @field:JsonProperty("data") @param:JsonProperty("data") val data: String
)