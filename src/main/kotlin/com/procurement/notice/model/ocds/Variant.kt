package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("hasVariants", "variantDetails")
data class Variant(

        @JsonProperty("hasVariants")
        @get:JsonProperty("hasVariants")
        val hasVariants: Boolean?,

        @JsonProperty("variantDetails")
        val variantDetails: String?
)