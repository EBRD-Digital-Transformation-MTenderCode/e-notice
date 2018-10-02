package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RecordTender @JsonCreator constructor(

        val id: String?,

        var title: String?,

        var description: String?,

        var status: TenderStatus?,

        var statusDetails: TenderStatusDetails?,

        var items: HashSet<Item>?,

        var lots: HashSet<Lot>?,

        val lotGroups: List<LotGroup>?,

        var tenderPeriod: Period?,

        var enquiryPeriod: Period?,

        var contractPeriod: Period?,

        var standstillPeriod: Period?,

        var awardPeriod: Period?,

        @get:JsonProperty("hasEnquiries")
        var hasEnquiries: Boolean? = false,

        var enquiries: HashSet<RecordEnquiry>?,

        var amendments: List<Amendment>?,

        var documents: HashSet<Document>?,

        val awardCriteria: String?,

        val submissionMethod: List<String>?,

        val submissionMethodDetails: String?,

        val submissionMethodRationale: List<String>?,

        @get:JsonProperty("requiresElectronicCatalogue")
        val requiresElectronicCatalogue: Boolean?
)

