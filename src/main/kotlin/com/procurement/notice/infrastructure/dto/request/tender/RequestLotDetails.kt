package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestLotDetails(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maximumLotsBidPerSupplier") @param:JsonProperty("maximumLotsBidPerSupplier") val maximumLotsBidPerSupplier: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maximumLotsAwardedPerSupplier") @param:JsonProperty("maximumLotsAwardedPerSupplier") val maximumLotsAwardedPerSupplier: Int?
)
