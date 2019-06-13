package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Item
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.LotGroup
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

@JsonInclude(JsonInclude.Include.NON_EMPTY)
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

        var standstillPeriod: Period?,

        var awardPeriod: Period?,

        var auctionPeriod: Period?,

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
        val requiresElectronicCatalogue: Boolean?,

        var procurementMethodModalities: Set<String>?,

        var electronicAuctions: ElectronicAuctions?
)