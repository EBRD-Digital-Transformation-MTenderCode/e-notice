package com.procurement.notice.infrastructure.dto.request.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestImplementation(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("transactions") @param:JsonProperty("transactions") val transactions: List<RequestTransaction> = emptyList()
)