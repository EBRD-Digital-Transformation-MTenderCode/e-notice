package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Contract @JsonCreator constructor(

        val id: String?,

        var date: LocalDateTime?,

        val awardId: String?,

        val title: String? = null,

        val description: String? = null,

        var status: String,

        var statusDetails: String,

        var documents: HashSet<Document>?,

        val extendsContractId: String? = null,

        val budgetSource: List<BudgetSource>? = null,

        val classification: Classification? = null,

        val period: Period? = null,

        val value: ValueTax? = null,

        val items: HashSet<Item>? = null,

        val dateSigned: LocalDateTime? = null,

        var relatedProcesses: HashSet<RelatedProcess>? = null,

        var amendments: List<Amendment>? = null,

        var amendment: Amendment? = null,

        val requirementResponses: HashSet<RequirementResponse>? = null,

        val countryOfOrigin: String? = null,

        val lotVariant: HashSet<String>? = null,

        val valueBreakdown: HashSet<ValueBreakdown>? = null,

        val isFrameworkOrDynamic: Boolean? = null,

        var agreedMetrics: LinkedList<AgreedMetric>? = null,

        var milestones: List<Milestone>? = null,

        var confirmationRequests: List<ConfirmationRequest>? = null,

        var confirmationResponses: HashSet<ConfirmationResponse>? = null

)



