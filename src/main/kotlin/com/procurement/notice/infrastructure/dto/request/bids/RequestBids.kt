package com.procurement.notice.infrastructure.dto.request.bids

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.awards.RequestBid
import com.procurement.notice.infrastructure.dto.request.awards.RequestBidsStatistic

data class RequestBids(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("statistics") @param:JsonProperty("statistics") val statistics: List<RequestBidsStatistic> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<RequestBid> = emptyList()
)
