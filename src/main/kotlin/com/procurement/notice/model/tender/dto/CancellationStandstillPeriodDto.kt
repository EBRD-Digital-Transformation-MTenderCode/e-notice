package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CancellationStandstillPeriodDto @JsonCreator constructor(

        val amendments: List<Amendment>,

        val standstillPeriod: Period,

        val lots: HashSet<Lot>?,

        val bids: HashSet<Bid>?,

        val awards: HashSet<Award>?

)
