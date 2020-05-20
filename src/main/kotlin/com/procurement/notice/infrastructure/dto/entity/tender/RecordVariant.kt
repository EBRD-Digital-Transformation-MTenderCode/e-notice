package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordVariant(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasVariants") @param:JsonProperty("hasVariants") val hasVariants: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("variantDetails") @param:JsonProperty("variantDetails") val variantDetails: String?
)
