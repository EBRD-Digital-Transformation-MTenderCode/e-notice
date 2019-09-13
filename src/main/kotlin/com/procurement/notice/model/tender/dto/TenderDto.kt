package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.record.ElectronicAuctions

data class TenderDto @JsonCreator constructor(

    val statusDetails: TenderStatusDetails,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val enquiryPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var auctionPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val procurementMethodModalities: Set<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val electronicAuctions: ElectronicAuctions?
)
