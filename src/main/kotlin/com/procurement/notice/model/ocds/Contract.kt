package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Contract @JsonCreator constructor(

        val id: String?,

        var date: LocalDateTime?,

        val awardId: String?,

        val title: String?,

        val description: String?,

        var status: String,

        var statusDetails: String,

        val extendsContractID: String?,

        val budgetSource: List<BudgetSource>?,

        val classification: Classification?,

        val period: Period?,

        val value: ValueTax?,

        val items: HashSet<Item>?,

        val dateSigned: LocalDateTime?,

        val documents: HashSet<Document>?,

        var relatedProcesses: HashSet<RelatedProcess>?,

        val amendments: List<Amendment>?,

        val amendment: Amendment?,

        val requirementResponses: HashSet<RequirementResponse>?,

        val countryOfOrigin: String?,

        val lotVariant: HashSet<String>?,

        val valueBreakdown: HashSet<ValueBreakdown>?,

        val isFrameworkOrDynamic: Boolean? = null,

        var agreedMetrics: LinkedList<AgreedMetric>? = null,

        val milestones: List<Milestone>?,

        var confirmationRequests: List<ConfirmationRequest>?

)



