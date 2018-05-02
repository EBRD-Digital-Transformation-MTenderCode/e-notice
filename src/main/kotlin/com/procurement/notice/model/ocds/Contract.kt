package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "id",
        "date",
        "awardID",
        "extendsContractID",
        "title",
        "description",
        "status",
        "statusDetails",
        "period",
        "value",
        "items",
        "dateSigned",
        "documents",
        "implementation",
        "relatedProcesses",
        "milestones",
        "amendments",
        "amendment",
        "requirementResponses",
        "countryOfOrigin",
        "lotVariant",
        "valueBreakdown",
        "isFrameworkOrDynamic")
data class Contract(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val date: LocalDateTime?,

        @JsonProperty("awardId")
        val awardId: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("status")
        var status: ContractStatus,

        @JsonProperty("statusDetails")
        var statusDetails: ContractStatusDetails,

        @JsonProperty("extendsContractID")
        val extendsContractID: String?,

        @JsonProperty("period")
        val period: Period?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("items")
        val items: HashSet<Item>?,

        @JsonProperty("dateSigned")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        val dateSigned: LocalDateTime?,

        @JsonProperty("documents")
        val documents: HashSet<Document>?,

        @JsonProperty("implementation")
        val implementation: Implementation?,

        @JsonProperty("relatedProcesses")
        var relatedProcesses: HashSet<RelatedProcess>?,

        @JsonProperty("milestones")
        val milestones: List<Milestone>?,

        @JsonProperty("amendments")
        val amendments: List<Amendment>?,

        @JsonProperty("amendment")
        val amendment: Amendment?,

        @JsonProperty("requirementResponses")
        val requirementResponses: HashSet<RequirementResponse>?,

        @JsonProperty("countryOfOrigin")
        val countryOfOrigin: String?,

        @JsonProperty("lotVariant")
        val lotVariant: HashSet<String>?,

        @JsonProperty("valueBreakdown")
        val valueBreakdown: HashSet<ValueBreakdown>?,

        @JsonProperty("isFrameworkOrDynamic")
        @get:JsonProperty("isFrameworkOrDynamic")
        val isFrameworkOrDynamic: Boolean?
)



