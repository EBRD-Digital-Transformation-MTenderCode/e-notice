package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordDynamicPurchasingSystem(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasDynamicPurchasingSystem") @param:JsonProperty("hasDynamicPurchasingSystem") val hasDynamicPurchasingSystem: Boolean?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasOutsideBuyerAccess") @param:JsonProperty("hasOutsideBuyerAccess") val hasOutsideBuyerAccess: Boolean?,

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("noFurtherContracts") @param:JsonProperty("noFurtherContracts") val noFurtherContracts: Boolean?
)
