package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "title", "description", "status", "statusDetails", "items", "minValue", "value", "procurementMethod", "procurementMethodDetails", "procurementMethodRationale", "mainProcurementCategory", "additionalProcurementCategories", "awardCriteria", "awardCriteriaDetails", "submissionMethod", "submissionMethodDetails", "tenderPeriod", "enquiryPeriod", "hasEnquiries", "enquiries", "eligibilityCriteria", "awardPeriod", "contractPeriod", "numberOfTenderers", "tenderers", "procuringEntity", "documents", "milestones", "amendments", "amendment", "lots", "lotDetails", "lotGroups", "participationFees", "criteria", "acceleratedProcedure", "classification", "designContest", "electronicWorkflows", "jointProcurement", "legalBasis", "objectives", "procedureOutsourcing", "procurementMethodAdditionalInfo", "reviewParties", "reviewPeriod", "standstillPeriod", "submissionLanguages", "submissionMethodRationale", "dynamicPurchasingSystem", "framework", "requiresElectronicCatalogue")
data class Tender(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("status")
        val status: TenderStatus?,

        @JsonProperty("statusDetails")
        val statusDetails: TenderStatusDetails?,

        @JsonProperty("items")
        val items: HashSet<Item>?,

        @JsonProperty("minValue")
        val minValue: Value?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("procurementMethod")
        val procurementMethod: ProcurementMethod?,

        @JsonProperty("procurementMethodDetails")
        val procurementMethodDetails: String?,

        @JsonProperty("procurementMethodRationale")
        val procurementMethodRationale: String?,

        @JsonProperty("mainProcurementCategory")
        val mainProcurementCategory: MainProcurementCategory?,

        @JsonProperty("additionalProcurementCategories")
        val additionalProcurementCategories: List<ExtendedProcurementCategory>?,

        @JsonProperty("awardCriteria")
        val awardCriteria: AwardCriteria?,

        @JsonProperty("awardCriteriaDetails")
        val awardCriteriaDetails: String?,

        @JsonProperty("submissionMethod")
        val submissionMethod: List<SubmissionMethod>?,

        @JsonProperty("submissionMethodDetails")
        val submissionMethodDetails: String?,

        @JsonProperty("tenderPeriod")
        val tenderPeriod: Period?,

        @JsonProperty("enquiryPeriod")
        val enquiryPeriod: Period?,

        @JsonProperty("hasEnquiries")
        @get:JsonProperty("hasEnquiries")
        val hasEnquiries: Boolean?,

        @JsonProperty("enquiries")
        val enquiries: List<Enquiry>?,

        @JsonProperty("eligibilityCriteria")
        val eligibilityCriteria: String?,

        @JsonProperty("awardPeriod")
        private val awardPeriod: Period?,

        @JsonProperty("contractPeriod")
        val contractPeriod: Period?,

        @JsonProperty("numberOfTenderers")
        val numberOfTenderers: Int?,

        @JsonProperty("tenderers")
        val tenderers: HashSet<OrganizationReference>?,

        @JsonProperty("procuringEntity")
        val procuringEntity: OrganizationReference?,

        @JsonProperty("documents")
        val documents: List<Document>?,

        @JsonProperty("milestones")
        val milestones: List<Milestone>?,

        @JsonProperty("amendments")
        val amendments: List<Amendment>?,

        @JsonProperty("amendment")
        val amendment: Amendment?,

        @JsonProperty("lots")
        val lots: List<Lot>?,

        @JsonProperty("lotDetails")
        val lotDetails: LotDetails?,

        @JsonProperty("lotGroups")
        val lotGroups: List<LotGroup>?,

        @JsonProperty("participationFees")
        val participationFees: HashSet<ParticipationFee>?,

        @JsonProperty("criteria")
        val criteria: List<Criterion>?,

        @JsonProperty("acceleratedProcedure")
        val acceleratedProcedure: AcceleratedProcedure?,

        @JsonProperty("classification")
        val classification: Classification?,

        @JsonProperty("designContest")
        val designContest: DesignContest?,

        @JsonProperty("electronicWorkflows")
        val electronicWorkflows: ElectronicWorkflows?,

        @JsonProperty("jointProcurement")
        val jointProcurement: JointProcurement?,

        @JsonProperty("legalBasis")
        val legalBasis: LegalBasis?,

        @JsonProperty("objectives")
        val objectives: Objectives?,

        @JsonProperty("procedureOutsourcing")
        val procedureOutsourcing: ProcedureOutsourcing?,

        @JsonProperty("procurementMethodAdditionalInfo")
        val procurementMethodAdditionalInfo: String?,

        @JsonProperty("reviewParties")
        val reviewParties: HashSet<OrganizationReference>?,

        @JsonProperty("reviewPeriod")
        val reviewPeriod: Period?,

        @JsonProperty("standstillPeriod")
        val standstillPeriod: Period?,

        @JsonProperty("submissionLanguages")
        val submissionLanguages: List<SubmissionLanguage>?,

        @JsonProperty("submissionMethodRationale")
        val submissionMethodRationale: List<SubmissionMethodRationale>?,

        @JsonProperty("dynamicPurchasingSystem")
        val dynamicPurchasingSystem: DynamicPurchasingSystem?,

        @JsonProperty("framework")
        val framework: Framework?,

        @JsonProperty("requiresElectronicCatalogue")
        @get:JsonProperty("requiresElectronicCatalogue")
        val requiresElectronicCatalogue: Boolean?
)
