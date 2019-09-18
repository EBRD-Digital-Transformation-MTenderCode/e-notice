package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.record.ElectronicAuctions
import java.util.*

data class AuctionPeriodEndDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatus: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatusDetails: TenderStatusDetails?,

    val awardPeriod: Period,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val awards: HashSet<Award>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val bids: HashSet<Bid>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: HashSet<Document>,

    val tender: AuctionTender
)

data class AuctionTender @JsonCreator constructor(

    val auctionPeriod: Period,

    var electronicAuctions: ElectronicAuctions
)
