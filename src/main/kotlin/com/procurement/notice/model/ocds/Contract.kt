package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.util.*

data class Contract @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var date: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val awardId: UUID?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val title: String? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String? = null,

    var status: String,

    var statusDetails: String,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var documents: HashSet<Document>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val extendsContractId: String? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val budgetSource: List<BudgetSource>? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val classification: Classification? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: ValueTax? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val items: HashSet<Item>? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dateSigned: LocalDateTime? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var relatedProcesses: HashSet<RelatedProcess>? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var amendments: List<Amendment>? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var amendment: Amendment? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val requirementResponses: HashSet<RequirementResponse>? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val countryOfOrigin: String? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lotVariant: HashSet<String>? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val valueBreakdown: HashSet<ValueBreakdown>? = null,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isFrameworkOrDynamic: Boolean? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var agreedMetrics: LinkedList<AgreedMetric>? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var milestones: List<Milestone>? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var confirmationRequests: List<ConfirmationRequest>? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    var confirmationResponses: HashSet<ConfirmationResponse>? = null

)



