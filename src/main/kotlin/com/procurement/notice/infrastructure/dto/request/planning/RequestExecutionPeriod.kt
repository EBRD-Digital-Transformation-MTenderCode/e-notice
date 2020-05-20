package com.procurement.notice.infrastructure.dto.request.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestExecutionPeriod(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("durationInDays") @param:JsonProperty("durationInDays") val durationInDays: Long?
)