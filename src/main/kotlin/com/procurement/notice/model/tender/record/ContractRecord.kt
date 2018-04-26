package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.model.ocds.*
import com.procurement.point.databinding.JsonDateDeserializer
import com.procurement.point.databinding.JsonDateSerializer
import java.time.LocalDateTime
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder(
        "ocid",
        "id",
        "date",
        "tag",
        "initiationType",
        "language",
        "parties",
        "awards",
        "contracts",
        "relatedProcesses")
data class ContractRecord(

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
        val language: String? = "en",

        @JsonProperty("parties")
        var parties: HashSet<Organization>?,

        @JsonProperty("awards")
        var awards: HashSet<Award>?,

        @JsonProperty("contracts")
        var contracts : HashSet<Contract>?,

        @JsonProperty("relatedProcesses")
        var relatedProcesses: HashSet<RelatedProcess>?
)
