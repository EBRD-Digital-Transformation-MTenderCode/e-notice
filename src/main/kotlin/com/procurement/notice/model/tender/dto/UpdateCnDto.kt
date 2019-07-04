package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class UpdateCnDto @JsonCreator constructor(

        val amendment: AmendmentUpdateCn?,

        val isAuctionPeriodChanged: Boolean?
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class AmendmentUpdateCn @JsonCreator constructor(

        val relatedLots: Set<String>
)