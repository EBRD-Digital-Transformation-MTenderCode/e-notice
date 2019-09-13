package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class Tender @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val status: TenderStatus?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val statusDetails: TenderStatusDetails?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val items: HashSet<Item>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val minValue: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethod: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodRationale: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val mainProcurementCategory: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val additionalProcurementCategories: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteria: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteriaDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethod: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val submissionMethodDetails: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val enquiryPeriod: Period?,

    @get:JsonProperty("hasEnquiries")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasEnquiries: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val enquiries: List<Enquiry>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val eligibilityCriteria: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private val awardPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val numberOfTenderers: Int?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val tenderers: HashSet<OrganizationReference>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procuringEntity: OrganizationReference?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val amendments: List<Amendment>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amendment: Amendment?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: List<Lot>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val lotDetails: LotDetails?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotGroups: List<LotGroup>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val participationFees: HashSet<ParticipationFee>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val criteria: List<Criterion>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val acceleratedProcedure: AcceleratedProcedure?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val classification: Classification?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val designContest: DesignContest?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val electronicWorkflows: ElectronicWorkflows?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val jointProcurement: JointProcurement?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val legalBasis: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val objectives: Objectives?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procedureOutsourcing: ProcedureOutsourcing?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodAdditionalInfo: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val reviewParties: HashSet<OrganizationReference>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val reviewPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val standstillPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionLanguages: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethodRationale: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dynamicPurchasingSystem: DynamicPurchasingSystem?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val framework: Framework?,

    @get:JsonProperty("requiresElectronicCatalogue")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val requiresElectronicCatalogue: Boolean?
)
