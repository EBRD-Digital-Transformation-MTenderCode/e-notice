package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.record.ElectronicAuctions

@JsonInclude(JsonInclude.Include.NON_NULL)
data class TenderDto @JsonCreator constructor(

        val statusDetails: TenderStatusDetails,

        val tenderPeriod: Period?,

        val enquiryPeriod: Period?,

        var auctionPeriod: Period?,

        val procurementMethodModalities: Set<String>?,

        val electronicAuctions: ElectronicAuctions?
)
