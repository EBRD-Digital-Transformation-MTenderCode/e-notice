package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.enums.TenderStatus
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
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.ocds.Value

data class MsTender @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var status: TenderStatus?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: TenderStatusDetails?,

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

    @get:JsonProperty("hasEnquiries")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    var hasEnquiries: Boolean? = false,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val eligibilityCriteria: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val contractPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var procuringEntity: OrganizationReference?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val amendments: List<Amendment>?,

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
    val procedureOutsourcing: ProcedureOutsourcing?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val procurementMethodAdditionalInfo: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val submissionLanguages: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dynamicPurchasingSystem: DynamicPurchasingSystem?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val framework: Framework?
)
