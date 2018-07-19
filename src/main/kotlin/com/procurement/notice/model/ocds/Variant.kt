package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Variant @JsonCreator constructor(

        @get:JsonProperty("hasVariants")
        val hasVariants: Boolean?,

        val variantDetails: String?
)