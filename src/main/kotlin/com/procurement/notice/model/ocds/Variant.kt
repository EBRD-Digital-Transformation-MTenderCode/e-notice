package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Variant @JsonCreator constructor(

        @get:JsonProperty("hasVariants")
        val hasVariants: Boolean?,

        val variantDetails: String?
)