package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Framework @JsonCreator constructor(

    @get:JsonProperty("isAFramework")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isAFramework: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val typeOfFramework: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val maxSuppliers: Int?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val exceptionalDurationRationale: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val additionalBuyerCategories: List<String>?
)