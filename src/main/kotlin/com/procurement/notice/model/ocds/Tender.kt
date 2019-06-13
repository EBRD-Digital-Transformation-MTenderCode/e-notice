package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class Tender @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        val status: TenderStatus?,

        val statusDetails: TenderStatusDetails?,

        val items: HashSet<Item>?,

        val minValue: Value?,

        val value: Value?,

        val procurementMethod: String?,

        val procurementMethodDetails: String?,

        val procurementMethodRationale: String?,

        val mainProcurementCategory: String?,

        val additionalProcurementCategories: List<String>?,

        val awardCriteria: String?,

        val awardCriteriaDetails: String?,

        val submissionMethod: List<String>?,

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

        val legalBasis: String?,

        val objectives: Objectives?,

        val procedureOutsourcing: ProcedureOutsourcing?,

        val procurementMethodAdditionalInfo: String?,

        val reviewParties: HashSet<OrganizationReference>?,

        val reviewPeriod: Period?,

        val standstillPeriod: Period?,

        val submissionLanguages: List<String>?,

        val submissionMethodRationale: List<String>?,

        val dynamicPurchasingSystem: DynamicPurchasingSystem?,

        val framework: Framework?,

        @get:JsonProperty("requiresElectronicCatalogue")
        val requiresElectronicCatalogue: Boolean?
)
