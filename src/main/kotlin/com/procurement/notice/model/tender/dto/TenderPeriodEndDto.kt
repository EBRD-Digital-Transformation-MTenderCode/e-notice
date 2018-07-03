package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.*
import java.util.*

data class TenderPeriodEndDto @JsonCreator constructor(

        val awardPeriod: Period,

        val awards: HashSet<Award>,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val documents: HashSet<Document>
)
