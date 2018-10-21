package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.record.ElectronicAuctions
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderPeriodEndDto @JsonCreator constructor(

        val tenderStatus: TenderStatus?,

        val tenderStatusDetails: TenderStatusDetails?,

        val awardPeriod: Period,

        val awards: HashSet<Award>,

        val lots: HashSet<Lot>,

        val bids: HashSet<Bid>,

        val documents: HashSet<Document>,

        var electronicAuctions: ElectronicAuctions?
)
