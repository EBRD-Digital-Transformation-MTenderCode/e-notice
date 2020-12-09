package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class UpdateCnDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amendment: AmendmentUpdateCn?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val isAuctionPeriodChanged: Boolean?
)

data class AmendmentUpdateCn @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>
)
