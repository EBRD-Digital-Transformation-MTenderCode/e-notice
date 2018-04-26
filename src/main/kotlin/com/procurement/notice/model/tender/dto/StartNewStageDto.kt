package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.tender.record.RecordTender

data class StartNewStageDto(

        @JsonProperty("tender")
        val tender: RecordTender,

        @JsonProperty("bids")
        val bids: Bids
)
