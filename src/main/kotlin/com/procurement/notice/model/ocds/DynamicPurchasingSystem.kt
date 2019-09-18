package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class DynamicPurchasingSystem @JsonCreator constructor(

    @get:JsonProperty("hasDynamicPurchasingSystem")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasDynamicPurchasingSystem: Boolean?,

    @get:JsonProperty("hasOutsideBuyerAccess")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasOutsideBuyerAccess: Boolean?,

    @get:JsonProperty("noFurtherContracts")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val noFurtherContracts: Boolean?
)
