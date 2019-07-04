package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class EndContractingProcessDto @JsonCreator constructor(

        val tender: EndContractingProcessTender,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val awards: HashSet<Award>,

        val awardPeriod: Period,

        val cans: HashSet<Can>
)

data class EndContractingProcessTender @JsonCreator constructor(

        var status: String,

        var statusDetails: String
)