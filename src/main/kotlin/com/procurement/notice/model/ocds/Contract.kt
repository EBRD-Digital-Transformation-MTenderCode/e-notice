package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.util.*

data class Contract @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

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

    var statusDetails: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>?,

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
    val items: HashSet<Item>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dateSigned: LocalDateTime? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: HashSet<RelatedProcess>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var amendments: List<Amendment>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var amendment: Amendment? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementResponses: HashSet<RequirementResponse>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val countryOfOrigin: String? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotVariant: HashSet<String>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val valueBreakdown: HashSet<ValueBreakdown>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val isFrameworkOrDynamic: Boolean? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var agreedMetrics: LinkedList<AgreedMetric>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var milestones: List<Milestone>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var confirmationRequests: List<ConfirmationRequest>? = null,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    var confirmationResponses: HashSet<ConfirmationResponse>? = null

)
