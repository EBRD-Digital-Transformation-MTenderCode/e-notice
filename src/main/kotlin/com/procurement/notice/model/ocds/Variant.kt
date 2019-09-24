package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Variant @JsonCreator constructor(

    @get:JsonProperty("hasVariants")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasVariants: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val variantDetails: String?
)
