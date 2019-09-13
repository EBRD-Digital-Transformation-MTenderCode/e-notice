package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class UpdateCnDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amendment: AmendmentUpdateCn?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isAuctionPeriodChanged: Boolean?
)

data class AmendmentUpdateCn @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: Set<String>
)