package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderStatusDto @JsonCreator constructor(

        val tenderStatus: TenderStatus?,
        val tenderStatusDetails: TenderStatusDetails?
)