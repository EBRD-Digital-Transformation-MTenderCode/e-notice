package com.procurement.notice.infrastructure.dto.entity.bids

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBid
import com.procurement.notice.infrastructure.dto.entity.awards.RecordBidsStatistic

data class RecordBids(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("statistics") @param:JsonProperty("statistics") val statistics: List<RecordBidsStatistic> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("details") @param:JsonProperty("details") val details: List<RecordBid> = emptyList()
)
