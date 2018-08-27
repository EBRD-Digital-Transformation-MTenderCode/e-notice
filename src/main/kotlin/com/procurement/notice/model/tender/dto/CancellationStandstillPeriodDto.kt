package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CancellationStandstillPeriodDto @JsonCreator constructor(

        val standstillPeriod: Period,

        val lots: HashSet<Lot>?,

        val bids: HashSet<Bid>?,

        val awards: HashSet<Award>?

)
