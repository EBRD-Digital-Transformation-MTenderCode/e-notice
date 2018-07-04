package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.tender.record.RecordTender

@JsonInclude(JsonInclude.Include.NON_NULL)
data class StartNewStageDto @JsonCreator constructor(

        val tender: RecordTender,

        val bids: Bids
)
