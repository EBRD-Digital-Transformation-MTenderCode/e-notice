package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class LotDetails @JsonCreator constructor(

        val maximumLotsBidPerSupplier: Int?,

        val maximumLotsAwardedPerSupplier: Int?
)