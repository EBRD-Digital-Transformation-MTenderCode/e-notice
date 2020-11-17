package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.infrastructure.dto.entity.tender.RecordOtherCriteria
import com.procurement.notice.model.ocds.Conversion
import com.procurement.notice.model.ocds.Criteria
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Item
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.LotGroup
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.ReleaseAmendment
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.tender.enquiry.RecordEnquiry

data class ReleaseTender (

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") var title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") var description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") var status: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") var statusDetails: TenderStatusDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("criteria") @param:JsonProperty("criteria") var criteria: List<Criteria> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("conversions") @param:JsonProperty("conversions") var conversions: List<Conversion> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("otherCriteria") @param:JsonProperty("otherCriteria") val otherCriteria: RecordOtherCriteria? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("items") @param:JsonProperty("items") var items: List<Item> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("lots") @param:JsonProperty("lots") var lots: List<Lot> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("lotGroups") @param:JsonProperty("lotGroups") var lotGroups: List<LotGroup> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("tenderPeriod") @param:JsonProperty("tenderPeriod") var tenderPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("enquiryPeriod") @param:JsonProperty("enquiryPeriod") var enquiryPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("standstillPeriod") @param:JsonProperty("standstillPeriod") var standstillPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardPeriod") @param:JsonProperty("awardPeriod") var awardPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("auctionPeriod") @param:JsonProperty("auctionPeriod") var auctionPeriod: Period?,

    @get:JsonProperty("hasEnquiries")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var hasEnquiries: Boolean? = false,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("enquiries") @param:JsonProperty("enquiries") var enquiries: MutableList<RecordEnquiry> = mutableListOf(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("amendments") @param:JsonProperty("amendments") var amendments: List<ReleaseAmendment> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") var documents: List<Document> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardCriteria") @param:JsonProperty("awardCriteria") var awardCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardCriteriaDetails") @param:JsonProperty("awardCriteriaDetails") var awardCriteriaDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("submissionMethod") @param:JsonProperty("submissionMethod") var submissionMethod: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("submissionMethodDetails") @param:JsonProperty("submissionMethodDetails") var submissionMethodDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("submissionMethodRationale") @param:JsonProperty("submissionMethodRationale") var submissionMethodRationale: List<String> = emptyList(),

    @get:JsonProperty("requiresElectronicCatalogue")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var requiresElectronicCatalogue: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("procurementMethodModalities") @param:JsonProperty("procurementMethodModalities") var procurementMethodModalities: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("electronicAuctions") @param:JsonProperty("electronicAuctions") var electronicAuctions: ElectronicAuctions?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("secondStage") @param:JsonProperty("secondStage") val secondStage: SecondStage?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procurementMethodRationale") @param:JsonProperty("procurementMethodRationale") val procurementMethodRationale: String?
)
