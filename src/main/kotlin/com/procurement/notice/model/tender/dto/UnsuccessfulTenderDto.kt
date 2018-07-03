package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import java.util.*

data class UnsuccessfulTenderDto @JsonCreator constructor(

        val lots: HashSet<Lot>?,

        val bids: HashSet<Bid>?,

        val awards: HashSet<Award>?
)
