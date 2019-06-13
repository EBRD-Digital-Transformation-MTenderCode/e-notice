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

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class MsTender @JsonCreator constructor(

        var id: String?,

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
