package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordLotDetails(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maximumLotsBidPerSupplier") @param:JsonProperty("maximumLotsBidPerSupplier") val maximumLotsBidPerSupplier: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maximumLotsAwardedPerSupplier") @param:JsonProperty("maximumLotsAwardedPerSupplier") val maximumLotsAwardedPerSupplier: Int?
)
