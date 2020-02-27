package com.procurement.notice.infrastructure.dto.entity.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordExecutionPeriod(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("durationInDays") @param:JsonProperty("durationInDays") val durationInDays: Long?
)