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

data class RecordTender @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var status: TenderStatus?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: TenderStatusDetails?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var items: HashSet<Item>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var lots: HashSet<Lot>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotGroups: List<LotGroup>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var tenderPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var enquiryPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var standstillPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var awardPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var auctionPeriod: Period?,

    @get:JsonProperty("hasEnquiries")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var hasEnquiries: Boolean? = false,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var enquiries: HashSet<RecordEnquiry>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var amendments: List<Amendment>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteria: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethod: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val submissionMethodDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethodRationale: List<String>?,

    @get:JsonProperty("requiresElectronicCatalogue")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val requiresElectronicCatalogue: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var procurementMethodModalities: Set<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var electronicAuctions: ElectronicAuctions?
)