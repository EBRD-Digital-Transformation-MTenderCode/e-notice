package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Contract @JsonCreator constructor(

        val id: String?,

        val date: LocalDateTime?,

        val awardId: String?,

        val title: String?,

        val description: String?,

        var status: ContractStatus,

        var statusDetails: ContractStatusDetails,

        val extendsContractID: String?,

        val period: Period?,

        val value: Value?,

        val items: HashSet<Item>?,

        val dateSigned: LocalDateTime?,

        val documents: HashSet<Document>?,

        val implementation: Implementation?,

        var relatedProcesses: HashSet<RelatedProcess>?,

        val milestones: List<Milestone>?,

        val amendments: List<Amendment>?,

        val amendment: Amendment?,

        val requirementResponses: HashSet<RequirementResponse>?,

        val countryOfOrigin: String?,

        val lotVariant: HashSet<String>?,

        val valueBreakdown: HashSet<ValueBreakdown>?,

        @get:JsonProperty("isFrameworkOrDynamic")
        val isFrameworkOrDynamic: Boolean?
)



