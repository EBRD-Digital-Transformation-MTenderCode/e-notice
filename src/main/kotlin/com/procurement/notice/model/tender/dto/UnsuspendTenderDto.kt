package com.procurement.notice.model.tender.dto

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

@JsonPropertyOrder("enquiry", "tender", "tenderPeriod", "enquiryPeriod")
data class UnsuspendTenderDto(

        @JsonProperty("enquiry")
        val enquiry: RecordEnquiry,

        @JsonProperty("tender")
        val tender: TenderDto,

        @JsonProperty("tenderPeriod")
        val tenderPeriod: Period,

        @JsonProperty("enquiryPeriod")
        val enquiryPeriod: Period
)
