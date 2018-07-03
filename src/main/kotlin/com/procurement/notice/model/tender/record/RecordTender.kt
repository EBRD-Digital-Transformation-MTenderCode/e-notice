package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.enquiry.RecordEnquiry


data class RecordTender @JsonCreator constructor(

        val id: String?,

        var title: String?,

        var description: String?,

        var status: TenderStatus?,

        var statusDetails: TenderStatusDetails?,

        val items: HashSet<Item>?,

        var lots: HashSet<Lot>?,

        val lotGroups: List<LotGroup>?,

        var tenderPeriod: Period?,

        var enquiryPeriod: Period?,

        var standstillPeriod: Period?,

        var awardPeriod: Period?,

        @get:JsonProperty("hasEnquiries")
        var hasEnquiries: Boolean? = false,

        var enquiries: HashSet<RecordEnquiry>?,

        val amendments: List<Amendment>?,

        var documents: HashSet<Document>?,

        val awardCriteria: AwardCriteria?,

        val submissionMethod: List<SubmissionMethod>?,

        val submissionMethodDetails: String?,

        val submissionMethodRationale: List<SubmissionMethodRationale>?,

        @get:JsonProperty("requiresElectronicCatalogue")
        val requiresElectronicCatalogue: Boolean?
)

