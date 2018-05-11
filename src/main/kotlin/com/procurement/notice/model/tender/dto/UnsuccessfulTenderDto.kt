package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.tender.record.RecordTender
import java.util.*

data class UnsuccessfulTenderDto(

        @JsonProperty("lots")
        val lots: HashSet<Lot>?,

        @JsonProperty("bids")
        val bids: HashSet<Bid>?,

        @JsonProperty("awards")
        val awards: HashSet<Award>?
)
