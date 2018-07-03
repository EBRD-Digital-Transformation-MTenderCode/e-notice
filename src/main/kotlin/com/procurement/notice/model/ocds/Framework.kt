package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Framework @JsonCreator constructor(

        @get:JsonProperty("isAFramework")
        val isAFramework: Boolean?,

        val typeOfFramework: TypeOfFramework?,

        val maxSuppliers: Int?,

        val exceptionalDurationRationale: String?,

        val additionalBuyerCategories: List<String>?
)