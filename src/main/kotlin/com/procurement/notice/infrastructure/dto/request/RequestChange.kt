package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestChange(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("property") @param:JsonProperty("property") val property: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("formerValue") @param:JsonProperty("formerValue") val formerValue: Any?
)
