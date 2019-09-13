package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Lot
import java.util.*

data class CanCancellationDto @JsonCreator constructor(

    val can: Contract,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val contract: Contract?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>,

    val lot: Lot
)

