package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class DynamicPurchasingSystem @JsonCreator constructor(

        @get:JsonProperty("hasDynamicPurchasingSystem")
        val hasDynamicPurchasingSystem: Boolean?,

        @get:JsonProperty("hasOutsideBuyerAccess")
        val hasOutsideBuyerAccess: Boolean?,

        @get:JsonProperty("noFurtherContracts")
        val noFurtherContracts: Boolean?
)
