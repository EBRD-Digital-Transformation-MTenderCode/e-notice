package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("hasVariants", "variantDetails")
data class Variant(

        @JsonProperty("hasVariants")
        val hasVariants: Boolean?,

        @JsonProperty("variantDetails")
        val variantDetails: String?
)