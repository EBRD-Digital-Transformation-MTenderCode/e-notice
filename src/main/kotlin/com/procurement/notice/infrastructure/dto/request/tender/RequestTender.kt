package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.infrastructure.dto.request.RequestAmendment
import com.procurement.notice.infrastructure.dto.request.RequestClassification
import com.procurement.notice.infrastructure.dto.request.RequestOrganizationReference
import com.procurement.notice.infrastructure.dto.request.RequestPeriod
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocument
import com.procurement.notice.model.ocds.RequestParticipationFee
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.ocds.Value
import com.procurement.notice.model.tender.record.SecondStage

data class RequestTender(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("id") @param:JsonProperty("id") val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("status") @param:JsonProperty("status") val status: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: TenderStatusDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("criteria") @param:JsonProperty("criteria") val criteria: List<RequestCriteria> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("conversions") @param:JsonProperty("conversions") val conversions: List<RequestConversion> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("items") @param:JsonProperty("items") val items: List<RequestItem> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("lots") @param:JsonProperty("lots") val lots: List<RequestLot> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("lotGroups") @param:JsonProperty("lotGroups") val lotGroups: List<RequestLotGroup> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("tenderPeriod") @param:JsonProperty("tenderPeriod") val tenderPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("enquiryPeriod") @param:JsonProperty("enquiryPeriod") val enquiryPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("standstillPeriod") @param:JsonProperty("standstillPeriod") val standstillPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardPeriod") @param:JsonProperty("awardPeriod") val awardPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("auctionPeriod") @param:JsonProperty("auctionPeriod") val auctionPeriod: RequestPeriod?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasEnquiries") @param:JsonProperty("hasEnquiries") val hasEnquiries: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("enquiries") @param:JsonProperty("enquiries") val enquiries: MutableList<RequestRecordEnquiry> = mutableListOf(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("amendments") @param:JsonProperty("amendments") val amendments: List<RequestAmendment> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<RequestDocument> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardCriteria") @param:JsonProperty("awardCriteria") val awardCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardCriteriaDetails") @param:JsonProperty("awardCriteriaDetails") val awardCriteriaDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("submissionMethod") @param:JsonProperty("submissionMethod") val submissionMethod: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("submissionMethodDetails") @param:JsonProperty("submissionMethodDetails") val submissionMethodDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("submissionMethodRationale") @param:JsonProperty("submissionMethodRationale") val submissionMethodRationale: List<String> = emptyList(),

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("requiresElectronicCatalogue") @param:JsonProperty("requiresElectronicCatalogue") val requiresElectronicCatalogue: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("procurementMethodModalities") @param:JsonProperty("procurementMethodModalities") val procurementMethodModalities: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("electronicAuctions") @param:JsonProperty("electronicAuctions") val electronicAuctions: RequestElectronicAuctions?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("minValue") @param:JsonProperty("minValue") val minValue: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procurementMethod") @param:JsonProperty("procurementMethod") val procurementMethod: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procurementMethodDetails") @param:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procurementMethodRationale") @param:JsonProperty("procurementMethodRationale") val procurementMethodRationale: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("mainProcurementCategory") @param:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalProcurementCategories") @param:JsonProperty("additionalProcurementCategories") val additionalProcurementCategories: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("eligibilityCriteria") @param:JsonProperty("eligibilityCriteria") val eligibilityCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("contractPeriod") @param:JsonProperty("contractPeriod") val contractPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("numberOfTenderers") @param:JsonProperty("numberOfTenderers") val numberOfTenderers: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("tenderers") @param:JsonProperty("tenderers") val tenderers: List<RequestOrganizationReference> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procuringEntity") @param:JsonProperty("procuringEntity") val procuringEntity: RequestOrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("milestones") @param:JsonProperty("milestones") val milestones: List<RequestMilestone> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amendment") @param:JsonProperty("amendment") val amendment: RequestAmendment?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("lotDetails") @param:JsonProperty("lotDetails") val lotDetails: RequestLotDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("participationFees") @param:JsonProperty("participationFees") val participationFees: List<RequestParticipationFee> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("acceleratedProcedure") @param:JsonProperty("acceleratedProcedure") val acceleratedProcedure: RequestAcceleratedProcedure?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: RequestClassification?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("designContest") @param:JsonProperty("designContest") val designContest: RequestDesignContest?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("electronicWorkflows") @param:JsonProperty("electronicWorkflows") val electronicWorkflows: RequestElectronicWorkflows?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("jointProcurement") @param:JsonProperty("jointProcurement") val jointProcurement: RequestJointProcurement?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("legalBasis") @param:JsonProperty("legalBasis") val legalBasis: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("objectives") @param:JsonProperty("objectives") val objectives: RequestObjectives?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procedureOutsourcing") @param:JsonProperty("procedureOutsourcing") val procedureOutsourcing: RequestProcedureOutsourcing?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("procurementMethodAdditionalInfo") @param:JsonProperty("procurementMethodAdditionalInfo") val procurementMethodAdditionalInfo: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("reviewParties") @param:JsonProperty("reviewParties") val reviewParties: List<RequestOrganizationReference> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("reviewPeriod") @param:JsonProperty("reviewPeriod") val reviewPeriod: RequestPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("submissionLanguages") @param:JsonProperty("submissionLanguages") val submissionLanguages: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("dynamicPurchasingSystem") @param:JsonProperty("dynamicPurchasingSystem") val dynamicPurchasingSystem: RequestDynamicPurchasingSystem?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("framework") @param:JsonProperty("framework") val framework: RequestFramework?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("secondStage") @param:JsonProperty("secondStage") val secondStage: SecondStage?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("targets") @param:JsonProperty("targets") val targets: List<RequestTarget> = emptyList()
)
