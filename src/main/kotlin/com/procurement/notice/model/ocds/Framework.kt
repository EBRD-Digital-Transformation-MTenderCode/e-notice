package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isAFramework", "typeOfFramework", "maxSuppliers", "exceptionalDurationRationale", "additionalBuyerCategories")
data class Framework(

        @JsonProperty("isAFramework")
        val isAFramework: Boolean?,

        @JsonProperty("typeOfFramework")
        val typeOfFramework: TypeOfFramework?,

        @JsonProperty("maxSuppliers")
        val maxSuppliers: Int?,

        @JsonProperty("exceptionalDurationRationale")
        val exceptionalDurationRationale: String?,

        @JsonProperty("additionalBuyerCategories")
        val additionalBuyerCategories: List<String>?
)