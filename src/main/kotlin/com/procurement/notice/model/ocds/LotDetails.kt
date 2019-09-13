package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class LotDetails @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val maximumLotsBidPerSupplier: Int?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val maximumLotsAwardedPerSupplier: Int?
)