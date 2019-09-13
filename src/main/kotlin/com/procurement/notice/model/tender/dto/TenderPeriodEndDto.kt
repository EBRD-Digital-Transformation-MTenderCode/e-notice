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

data class TenderPeriodEndDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatus: TenderStatus?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatusDetails: TenderStatusDetails?,

    val awardPeriod: Period,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: HashSet<Lot>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: HashSet<Document>
)
