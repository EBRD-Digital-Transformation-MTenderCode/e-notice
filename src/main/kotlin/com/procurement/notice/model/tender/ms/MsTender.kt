package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.AcceleratedProcedure
import com.procurement.notice.model.ocds.Amendment
import com.procurement.notice.model.ocds.Classification
import com.procurement.notice.model.ocds.DesignContest
import com.procurement.notice.model.ocds.DynamicPurchasingSystem
import com.procurement.notice.model.ocds.ElectronicWorkflows
import com.procurement.notice.model.ocds.Framework
import com.procurement.notice.model.ocds.JointProcurement
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.ProcedureOutsourcing
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.ocds.Value

data class MsTender @JsonCreator constructor(

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var id: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val title: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val description: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var status: TenderStatus?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var statusDetails: TenderStatusDetails?,

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
        @get:JsonProperty("hasEnquiries")
        var hasEnquiries: Boolean? = false,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val eligibilityCriteria: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val contractPeriod: Period?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        var procuringEntity: OrganizationReference?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val amendments: List<Amendment>?,

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
        val procedureOutsourcing: ProcedureOutsourcing?,

        val procurementMethodAdditionalInfo: String?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        val submissionLanguages: List<String>?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val dynamicPurchasingSystem: DynamicPurchasingSystem?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        val framework: Framework?
)
