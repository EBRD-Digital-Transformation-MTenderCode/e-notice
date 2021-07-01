package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestVariant(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasVariants") @param:JsonProperty("hasVariants") val hasVariants: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("variantsDetails") @param:JsonProperty("variantsDetails") val variantsDetails: String?
)
