package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestFramework(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isAFramework") @param:JsonProperty("isAFramework") val isAFramework: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("typeOfFramework") @param:JsonProperty("typeOfFramework") val typeOfFramework: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maxSuppliers") @param:JsonProperty("maxSuppliers") val maxSuppliers: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("exceptionalDurationRationale") @param:JsonProperty("exceptionalDurationRationale") val exceptionalDurationRationale: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalBuyerCategories") @param:JsonProperty("additionalBuyerCategories") val additionalBuyerCategories: List<String> = emptyList()
)
