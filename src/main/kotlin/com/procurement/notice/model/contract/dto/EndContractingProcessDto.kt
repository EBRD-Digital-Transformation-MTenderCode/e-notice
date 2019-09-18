package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period

data class EndContractingProcessDto @JsonCreator constructor(

    val tender: EndContractingProcessTender,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    val awardPeriod: Period,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val cans: HashSet<Can>
)

data class EndContractingProcessTender @JsonCreator constructor(

    var status: String,

    var statusDetails: String
)
