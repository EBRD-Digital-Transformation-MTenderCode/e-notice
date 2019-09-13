package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails

data class TenderStatusDto @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatus: TenderStatus?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatusDetails: TenderStatusDetails?
)