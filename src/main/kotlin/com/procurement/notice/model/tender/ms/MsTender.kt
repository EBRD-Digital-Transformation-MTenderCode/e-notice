package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.procurement.notice.model.ocds.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "id",
        "title",
        "description",
        "classification",
        "status",
        "statusDetails",
        "value",
        "procurementMethod",
        "procurementMethodDetails",
        "procurementMethodRationale",
        "procurementMethodAdditionalInfo",
        "mainProcurementCategory",
        "additionalProcurementCategories",
        "hasEnquiries",
        "eligibilityCriteria",
        "submissionLanguages",
        "amendments",
        "contractPeriod",
        "acceleratedProcedure",
        "designContest",
        "electronicWorkflows",
        "jointProcurement",
        "procedureOutsourcing",
        "framework",
        "dynamicPurchasingSystem",
        "legalBasis",
        "procuringEntity")
data class MsTender(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("status")
        val status: TenderStatus?,

        @JsonProperty("statusDetails")
        var statusDetails: TenderStatusDetails?,

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

        @JsonProperty("hasEnquiries")
        @get:JsonProperty("hasEnquiries")
        var hasEnquiries: Boolean?,

        @JsonProperty("eligibilityCriteria")
        val eligibilityCriteria: String?,

        @JsonProperty("contractPeriod")
        val contractPeriod: Period?,

        @JsonProperty("procuringEntity")
        var procuringEntity: OrganizationReference?,

        @JsonProperty("amendments")
        val amendments: List<Amendment>?,

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

        @JsonProperty("procedureOutsourcing")
        val procedureOutsourcing: ProcedureOutsourcing?,

        @JsonProperty("procurementMethodAdditionalInfo")
        val procurementMethodAdditionalInfo: String?,

        @JsonProperty("submissionLanguages")
        val submissionLanguages: List<SubmissionLanguage>?,

        @JsonProperty("dynamicPurchasingSystem")
        val dynamicPurchasingSystem: DynamicPurchasingSystem?,

        @JsonProperty("framework")
        val framework: Framework?
)
