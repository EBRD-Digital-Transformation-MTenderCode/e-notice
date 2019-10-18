package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Conversion
import com.procurement.notice.model.ocds.Criteria
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Item
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.LotGroup
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

data class RecordTender @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: TenderStatusDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var criteria: List<Criteria>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var conversions: List<Conversion>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var items: HashSet<Item>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var lots: HashSet<Lot>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotGroups: List<LotGroup>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var tenderPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var enquiryPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var standstillPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var awardPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var auctionPeriod: Period?,

    @get:JsonProperty("hasEnquiries")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var hasEnquiries: Boolean? = false,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var enquiries: HashSet<RecordEnquiry>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var amendments: List<Amendment>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteriaDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethod: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val submissionMethodDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethodRationale: List<String>?,

    @get:JsonProperty("requiresElectronicCatalogue")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val requiresElectronicCatalogue: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var procurementMethodModalities: Set<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var electronicAuctions: ElectronicAuctions?
)
