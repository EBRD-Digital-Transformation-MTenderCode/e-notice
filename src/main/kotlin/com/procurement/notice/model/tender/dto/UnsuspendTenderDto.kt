package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

data class UnsuspendTenderDto @JsonCreator constructor(

    val enquiry: RecordEnquiry,

    val tender: TenderDto
)
