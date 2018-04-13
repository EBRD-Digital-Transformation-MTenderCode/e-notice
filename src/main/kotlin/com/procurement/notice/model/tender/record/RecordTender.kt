package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "id",
        "title",
        "description",
        "status",
        "statusDetails",
        "lots",
        "lotGroups",
        "items",
        "tenderPeriod",
        "enquiryPeriod",
        "standstillPeriod",
        "awardPeriod",
        "hasEnquiries",
        "enquiries",
        "amendments",
        "documents",
        "awardCriteria",
        "submissionMethod",
        "submissionMethodDetails",
        "submissionMethodRationale",
        "requiresElectronicCatalogue")
data class RecordTender(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        var title: String?,

        @JsonProperty("description")
        var description: String?,

        @JsonProperty("status")
        var status: TenderStatus?,

        @JsonProperty("statusDetails")
        var statusDetails: TenderStatusDetails?,

        @JsonProperty("items")
        val items: HashSet<Item>,

        @JsonProperty("lots")
        var lots: List<Lot>?,

        @JsonProperty("lotGroups")
        val lotGroups: List<LotGroup>?,

        @JsonProperty("tenderPeriod")
        var tenderPeriod: Period?,

        @JsonProperty("enquiryPeriod")
        var enquiryPeriod: Period?,

        @JsonProperty("standstillPeriod")
        var standstillPeriod: Period?,

        @JsonProperty("awardPeriod")
        var awardPeriod: Period?,

        @JsonProperty("hasEnquiries")
        val hasEnquiries: Boolean?,

        @JsonProperty("enquiries")
        val enquiries: ArrayList<RecordEnquiry>?,

        @JsonProperty("amendments")
        val amendments: List<Amendment>?,

        @JsonProperty("documents")
        var documents: List<Document>?,

        @JsonProperty("awardCriteria")
        val awardCriteria: AwardCriteria?,

        @JsonProperty("submissionMethod")
        val submissionMethod: List<SubmissionMethod>?,

        @JsonProperty("submissionMethodDetails")
        val submissionMethodDetails: String?,

        @JsonProperty("submissionMethodRationale")
        val submissionMethodRationale: List<SubmissionMethodRationale>?,

        @JsonProperty("requiresElectronicCatalogue")
        val requiresElectronicCatalogue: Boolean?
)

