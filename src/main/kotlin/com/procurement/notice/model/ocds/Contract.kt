package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

data class Contract @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var internalId: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val awardId: UUID?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String? = null,

    var status: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var statusDetails: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: List<Document>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val extendsContractId: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetSource: List<BudgetSource>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val classification: Classification? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: ValueTax? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val items: List<Item>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dateSigned: LocalDateTime? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: MutableList<RelatedProcess>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var amendments: List<Amendment>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var amendment: Amendment? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementResponses: List<RequirementResponse>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val countryOfOrigin: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotVariant: List<String>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val valueBreakdown: List<ValueBreakdown>? = null,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val isFrameworkOrDynamic: Boolean? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var agreedMetrics: LinkedList<AgreedMetric>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var milestones: List<Milestone>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var confirmationRequests: List<ConfirmationRequest>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var confirmationResponses: List<ConfirmationResponse>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("suppliers") @param:JsonProperty("suppliers") val suppliers: List<OrganizationReference> = emptyList()
)
