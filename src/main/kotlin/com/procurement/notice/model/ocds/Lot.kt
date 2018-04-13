package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("id", "title", "description", "status", "statusDetails", "value", "options", "recurrentProcurement", "renewals", "variants", "contractPeriod", "placeOfPerformance")
data class Lot(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("title")
        val title: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("status")
        val status: TenderStatus?,

        @JsonProperty("statusDetails")
        var statusDetails: TenderStatusDetails?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("options")
        val options: List<Option>?,

        @JsonProperty("recurrentProcurement")
        val recurrentProcurement: List<RecurrentProcurement>?,

        @JsonProperty("renewals")
        val renewals: List<Renewal>?,

        @JsonProperty("variants")
        val variants: List<Variant>?,

        @JsonProperty("contractPeriod")
        val contractPeriod: Period?,

        @JsonProperty("placeOfPerformance")
        val placeOfPerformance: PlaceOfPerformance?
)
