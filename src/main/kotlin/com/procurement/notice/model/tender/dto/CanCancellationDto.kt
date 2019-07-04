package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Lot
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class CanCancellationDto @JsonCreator constructor(

    val can: Contract,

    val contract: Contract?,

    val awards: HashSet<Award>,

    val bids: HashSet<Bid>,

    val lot: Lot
)

