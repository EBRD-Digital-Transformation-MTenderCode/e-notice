package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class LotDetails @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val maximumLotsBidPerSupplier: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val maximumLotsAwardedPerSupplier: Int?
)
