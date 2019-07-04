package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class TenderPeriodEndDto @JsonCreator constructor(

        val tenderStatus: TenderStatus?,

        val tenderStatusDetails: TenderStatusDetails?,

        val awardPeriod: Period,

        val awards: HashSet<Award>,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val documents: HashSet<Document>
)
