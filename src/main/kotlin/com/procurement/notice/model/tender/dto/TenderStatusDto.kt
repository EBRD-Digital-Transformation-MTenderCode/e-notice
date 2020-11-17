package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails

data class TenderStatusDto @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatus: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderStatusDetails: TenderStatusDetails?
)
