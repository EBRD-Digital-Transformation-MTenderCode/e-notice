package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("hasDynamicPurchasingSystem", "hasOutsideBuyerAccess", "noFurtherContracts")
data class DynamicPurchasingSystem(

        @JsonProperty("hasDynamicPurchasingSystem")
        val hasDynamicPurchasingSystem: Boolean?,

        @JsonProperty("hasOutsideBuyerAccess")
        val hasOutsideBuyerAccess: Boolean?,

        @JsonProperty("noFurtherContracts")
        val noFurtherContracts: Boolean?
)
