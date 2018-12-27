package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.*

data class EndAwardPeriodDto @JsonCreator constructor(

        val contract: EndAwardPeriodContract?,

        val tender: EndAwardPeriodTender,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val awards: HashSet<Award>,

        val awardPeriod: Period,

        val cans: HashSet<Can>
)

data class EndAwardPeriodContract @JsonCreator constructor(

        var status: String,

        var statusDetails: String,

        val milestones: List<Milestone>
)

data class EndAwardPeriodTender @JsonCreator constructor(

        var status: String,

        var statusDetails: String
)