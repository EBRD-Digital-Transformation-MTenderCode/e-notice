package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Framework @JsonCreator constructor(

        @get:JsonProperty("isAFramework")
        val isAFramework: Boolean?,

        val typeOfFramework: TypeOfFramework?,

        val maxSuppliers: Int?,

        val exceptionalDurationRationale: String?,

        val additionalBuyerCategories: List<String>?
)