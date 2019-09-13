package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Milestone
import com.procurement.notice.model.ocds.Period

data class EndAwardPeriodDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val contract: EndAwardPeriodContract?,

    val tender: EndAwardPeriodTender,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    val awardPeriod: Period,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val cans: HashSet<Can>
)

data class EndAwardPeriodContract @JsonCreator constructor(

    var status: String,

    var statusDetails: String,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>
)

data class EndAwardPeriodTender @JsonCreator constructor(

    var status: String,

    var statusDetails: String
)