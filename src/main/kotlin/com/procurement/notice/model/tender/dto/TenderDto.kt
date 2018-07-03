package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatusDetails

data class TenderDto @JsonCreator constructor(

        val statusDetails: TenderStatusDetails,

        val tenderPeriod: Period?,

        val enquiryPeriod: Period?
)
