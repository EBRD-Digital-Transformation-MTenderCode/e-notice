package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class DynamicPurchasingSystem @JsonCreator constructor(

    @get:JsonProperty("hasDynamicPurchasingSystem")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasDynamicPurchasingSystem: Boolean?,

    @get:JsonProperty("hasOutsideBuyerAccess")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val hasOutsideBuyerAccess: Boolean?,

    @get:JsonProperty("noFurtherContracts")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val noFurtherContracts: Boolean?
)
