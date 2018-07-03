package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class LotDetails @JsonCreator constructor(

        val maximumLotsBidPerSupplier: Int?,

        val maximumLotsAwardedPerSupplier: Int?
)