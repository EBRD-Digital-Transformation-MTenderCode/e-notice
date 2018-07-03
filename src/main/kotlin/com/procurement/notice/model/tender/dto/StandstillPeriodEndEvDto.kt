package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Period

data class StandstillPeriodEndEvDto @JsonCreator constructor(

        val standstillPeriod: Period,

        val cans: HashSet<Can>
)
