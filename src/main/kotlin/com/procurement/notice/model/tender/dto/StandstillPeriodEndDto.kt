package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

data class StandstillPeriodEndDto @JsonCreator constructor(

    val standstillPeriod: Period,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>
)
