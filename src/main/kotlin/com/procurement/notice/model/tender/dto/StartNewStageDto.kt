package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.tender.record.ReleaseTender

data class StartNewStageDto @JsonCreator constructor(

    val tender: ReleaseTender,

    val bids: Bids
)
