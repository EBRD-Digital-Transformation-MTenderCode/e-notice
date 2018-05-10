package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.tender.record.RecordTender
import java.util.*

data class UnsuccessfulTenderDto(

        @JsonProperty("tender")
        var tender: RecordTender,

        @JsonProperty("bids")
        val bids: HashSet<Bid>?
)
