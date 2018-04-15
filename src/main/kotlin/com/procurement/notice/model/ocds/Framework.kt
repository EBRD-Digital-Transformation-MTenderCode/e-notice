package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("isAFramework", "typeOfFramework", "maxSuppliers", "exceptionalDurationRationale", "additionalBuyerCategories")
data class Framework(

        @JsonProperty("isAFramework")
        @get:JsonProperty("isAFramework")
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