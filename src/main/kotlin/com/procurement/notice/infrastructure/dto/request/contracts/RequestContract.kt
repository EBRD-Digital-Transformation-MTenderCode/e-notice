package com.procurement.notice.infrastructure.dto.request.contracts

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.RequestAgreedMetric
import com.procurement.notice.infrastructure.dto.request.RequestAmendment
import com.procurement.notice.infrastructure.dto.request.RequestClassification
import com.procurement.notice.infrastructure.dto.request.RequestOrganizationReference
import com.procurement.notice.infrastructure.dto.request.RequestPeriod
import com.procurement.notice.infrastructure.dto.request.RequestRelatedProcess
import com.procurement.notice.infrastructure.dto.request.awards.RequestRequirementResponse
import com.procurement.notice.infrastructure.dto.request.documents.RequestDocument
import com.procurement.notice.infrastructure.dto.request.tender.RequestItem
import com.procurement.notice.infrastructure.dto.request.tender.RequestMilestone
import java.time.LocalDateTime
import java.util.*

data class RequestContract(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("awardId") @param:JsonProperty("awardId") val awardId: UUID?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("title") @param:JsonProperty("title") val title: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String? = null,

    @field:JsonProperty("status") @param:JsonProperty("status") val status: String,

    @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<RequestDocument> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("extendsContractId") @param:JsonProperty("extendsContractId") val extendsContractId: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("budgetSource") @param:JsonProperty("budgetSource") val budgetSource: List<RequestBudgetSource> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: RequestClassification? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RequestPeriod? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: RequestValueTax? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("items") @param:JsonProperty("items") val items: List<RequestItem> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("dateSigned") @param:JsonProperty("dateSigned") val dateSigned: LocalDateTime? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("relatedProcesses") @param:JsonProperty("relatedProcesses") val relatedProcesses: List<RequestRelatedProcess> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("amendments") @param:JsonProperty("amendments") val amendments: List<RequestAmendment> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amendment") @param:JsonProperty("amendment") val amendment: RequestAmendment? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("requirementResponses") @param:JsonProperty("requirementResponses") val requirementResponses: List<RequestRequirementResponse> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("countryOfOrigin") @param:JsonProperty("countryOfOrigin") val countryOfOrigin: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("lotVariant") @param:JsonProperty("lotVariant") val lotVariant: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("valueBreakdown") @param:JsonProperty("valueBreakdown") val valueBreakdown: List<RequestValueBreakdown> = emptyList(),

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isFrameworkOrDynamic") @param:JsonProperty("isFrameworkOrDynamic") val isFrameworkOrDynamic: Boolean? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("agreedMetrics") @param:JsonProperty("agreedMetrics") val agreedMetrics: List<RequestAgreedMetric> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("milestones") @param:JsonProperty("milestones") val milestones: List<RequestMilestone> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("confirmationRequests") @param:JsonProperty("confirmationRequests") val confirmationRequests: List<RequestConfirmationRequest> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("confirmationResponses") @param:JsonProperty("confirmationResponses") val confirmationResponses: List<RequestConfirmationResponse> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("suppliers") @param:JsonProperty("suppliers") val suppliers: List<RequestOrganizationReference> = emptyList()
)
