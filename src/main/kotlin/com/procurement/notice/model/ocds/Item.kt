package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.procurement.notice.databinding.MoneyDeserializer
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
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
        val quantity: BigDecimal?,

        @JsonProperty("unit")
        val unit: Unit?,

        @JsonProperty("relatedLot")
        val relatedLot: String?
)