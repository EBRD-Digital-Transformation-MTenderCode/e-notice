package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("id", "description", "classification", "additionalClassifications", "quantity", "unit", "relatedLot")
data class Item(

        @JsonProperty("id")
        val id: String?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("classification")
        val classification: Classification?,

        @JsonProperty("additionalClassifications")
        val additionalClassifications: HashSet<Classification>?,

        @JsonProperty("quantity")
        val quantity: Double?,

        @JsonProperty("unit")
        val unit: Unit?,

        @JsonProperty("relatedLot")
        val relatedLot: String?
)