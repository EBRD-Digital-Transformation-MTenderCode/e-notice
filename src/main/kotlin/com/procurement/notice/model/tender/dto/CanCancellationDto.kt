package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.Lot
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class CanCancellationDto @JsonCreator constructor(

    val can: Contract,
    val awards: HashSet<Award>,
    val bids: CanCancellationBid,
    val lot: HashSet<Lot>
)

data class CanCancellationBid @JsonCreator constructor(
    val details: HashSet<Bid>
)

