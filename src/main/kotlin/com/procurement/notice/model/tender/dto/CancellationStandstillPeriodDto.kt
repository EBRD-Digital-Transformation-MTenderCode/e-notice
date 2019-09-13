package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

data class CancellationStandstillPeriodDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val amendments: List<Amendment>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val standstillPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>?

)
