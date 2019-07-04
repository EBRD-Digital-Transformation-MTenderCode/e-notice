package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Milestone
import com.procurement.notice.model.ocds.Period

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class EndAwardPeriodDto @JsonCreator constructor(

        val contract: EndAwardPeriodContract?,

        val tender: EndAwardPeriodTender,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val awards: HashSet<Award>,

        val awardPeriod: Period,

        val cans: HashSet<Can>
)

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class EndAwardPeriodContract @JsonCreator constructor(

        var status: String,

        var statusDetails: String,

        val milestones: List<Milestone>
)

data class EndAwardPeriodTender @JsonCreator constructor(

        var status: String,

        var statusDetails: String
)