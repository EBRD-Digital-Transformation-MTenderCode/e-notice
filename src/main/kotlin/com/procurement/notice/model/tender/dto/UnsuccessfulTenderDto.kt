package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period
import java.util.*

data class UnsuccessfulTenderDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val awardPeriod: Period?

)
