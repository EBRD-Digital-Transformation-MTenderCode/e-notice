package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class MsTender @JsonCreator constructor(

        val id: String?,

        val title: String?,

        val description: String?,

        var status: TenderStatus?,

        var statusDetails: TenderStatusDetails?,

        val value: Value?,

        val procurementMethod: String?,

        val procurementMethodDetails: String?,

        val procurementMethodRationale: String?,

        val mainProcurementCategory: String?,

        val additionalProcurementCategories: List<String>?,

        @get:JsonProperty("hasEnquiries")
        var hasEnquiries: Boolean? = false,

        val eligibilityCriteria: String?,

        val contractPeriod: Period?,

        var procuringEntity: OrganizationReference?,

        val amendments: List<Amendment>?,

        val acceleratedProcedure: AcceleratedProcedure?,

        val classification: Classification?,

        val designContest: DesignContest?,

        val electronicWorkflows: ElectronicWorkflows?,

        val jointProcurement: JointProcurement?,

        val legalBasis: String?,

        val procedureOutsourcing: ProcedureOutsourcing?,

        val procurementMethodAdditionalInfo: String?,

        val submissionLanguages: List<String>?,

        val dynamicPurchasingSystem: DynamicPurchasingSystem?,

        val framework: Framework?
)
