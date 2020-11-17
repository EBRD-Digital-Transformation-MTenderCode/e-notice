package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.enums.TenderStatus
import java.util.*

data class Tender @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val status: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val statusDetails: TenderStatusDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val items: HashSet<Item>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val minValue: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethod: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodRationale: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val mainProcurementCategory: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val additionalProcurementCategories: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val awardCriteriaDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethod: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val submissionMethodDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val tenderPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val enquiryPeriod: Period?,

    @get:JsonProperty("hasEnquiries")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasEnquiries: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val enquiries: List<Enquiry>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val eligibilityCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    private val awardPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val numberOfTenderers: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val tenderers: HashSet<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procuringEntity: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val documents: List<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val amendments: List<Amendment>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amendment: Amendment?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: List<Lot>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val lotDetails: LotDetails?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotGroups: List<LotGroup>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val participationFees: HashSet<ParticipationFee>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val criteria: List<Criterion>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val acceleratedProcedure: AcceleratedProcedure?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val classification: Classification?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val designContest: DesignContest?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val electronicWorkflows: ElectronicWorkflows?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val jointProcurement: JointProcurement?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val legalBasis: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val objectives: Objectives?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procedureOutsourcing: ProcedureOutsourcing?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodAdditionalInfo: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val reviewParties: HashSet<OrganizationReference>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val reviewPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val standstillPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionLanguages: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionMethodRationale: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dynamicPurchasingSystem: DynamicPurchasingSystem?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val framework: Framework?,

    @get:JsonProperty("requiresElectronicCatalogue")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val requiresElectronicCatalogue: Boolean?
)
