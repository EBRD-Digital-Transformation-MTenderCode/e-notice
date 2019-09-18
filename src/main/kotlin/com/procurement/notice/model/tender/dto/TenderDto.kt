package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.record.ElectronicAuctions

data class TenderDto @JsonCreator constructor(

    val statusDetails: TenderStatusDetails,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val enquiryPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var auctionPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val procurementMethodModalities: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val electronicAuctions: ElectronicAuctions?
)
