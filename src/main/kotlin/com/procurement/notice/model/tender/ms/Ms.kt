package com.procurement.notice.model.tender.ms

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.RelatedProcess
import com.procurement.notice.model.ocds.Tag
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("ocid", "id", "date", "tag", "initiationType", "language", "planning", "tender", "parties", "relatedProcesses")
data class Ms(

        @JsonProperty("ocid")
        var ocid: String?,

        @JsonProperty("id")
        var id: String?,

        @JsonProperty("date")
        @JsonDeserialize(using = JsonDateDeserializer::class)
        @JsonSerialize(using = JsonDateSerializer::class)
        var date: LocalDateTime?,

        @JsonProperty("tag")
        var tag: List<Tag>?,

        @JsonProperty("initiationType")
        var initiationType: InitiationType?,

        @JsonProperty("language")
        val language: String?,

        @JsonProperty("planning")
        val planning: MsPlanning?,

        @JsonProperty("tender")
        var tender: MsTender,

        @JsonProperty("parties")
        var parties: HashSet<Organization>?,

        @JsonProperty("relatedProcesses")
        var relatedProcesses: HashSet<RelatedProcess>?
)
