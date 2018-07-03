package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Tender @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val status: TenderStatus?,

        val statusDetails: TenderStatusDetails?,

        val items: HashSet<Item>?,

        val minValue: Value?,

        val value: Value?,

        val procurementMethod: ProcurementMethod?,

        val procurementMethodDetails: String?,

        val procurementMethodRationale: String?,

        val mainProcurementCategory: MainProcurementCategory?,

        val additionalProcurementCategories: List<ExtendedProcurementCategory>?,

        val awardCriteria: AwardCriteria?,

        val awardCriteriaDetails: String?,

        val submissionMethod: List<SubmissionMethod>?,

        val submissionMethodDetails: String?,

        val tenderPeriod: Period?,

        val enquiryPeriod: Period?,

        @get:JsonProperty("hasEnquiries")
        val hasEnquiries: Boolean?,

        val enquiries: List<Enquiry>?,

        val eligibilityCriteria: String?,

        private val awardPeriod: Period?,

        val contractPeriod: Period?,

        val numberOfTenderers: Int?,

        val tenderers: HashSet<OrganizationReference>?,

        val procuringEntity: OrganizationReference?,

        val documents: List<Document>?,

        val milestones: List<Milestone>?,

        val amendments: List<Amendment>?,

        val amendment: Amendment?,

        val lots: List<Lot>?,

        val lotDetails: LotDetails?,

        val lotGroups: List<LotGroup>?,

        val participationFees: HashSet<ParticipationFee>?,

        val criteria: List<Criterion>?,

        val acceleratedProcedure: AcceleratedProcedure?,

        val classification: Classification?,

        val designContest: DesignContest?,

        val electronicWorkflows: ElectronicWorkflows?,

        val jointProcurement: JointProcurement?,

        val legalBasis: LegalBasis?,

        val objectives: Objectives?,

        val procedureOutsourcing: ProcedureOutsourcing?,

        val procurementMethodAdditionalInfo: String?,

        val reviewParties: HashSet<OrganizationReference>?,

        val reviewPeriod: Period?,

        val standstillPeriod: Period?,

        val submissionLanguages: List<SubmissionLanguage>?,

        val submissionMethodRationale: List<SubmissionMethodRationale>?,

        val dynamicPurchasingSystem: DynamicPurchasingSystem?,

        val framework: Framework?,

        @get:JsonProperty("requiresElectronicCatalogue")
        val requiresElectronicCatalogue: Boolean?
)
