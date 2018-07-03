package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

data class StandstillPeriodEndDto @JsonCreator constructor(

        val standstillPeriod: Period,

        val lots: HashSet<Lot>
)
