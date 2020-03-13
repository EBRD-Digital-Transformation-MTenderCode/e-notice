package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordRenewal(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("hasRenewals") @param:JsonProperty("hasRenewals") val hasRenewals: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maxNumber") @param:JsonProperty("maxNumber") val maxNumber: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("renewalConditions") @param:JsonProperty("renewalConditions") val renewalConditions: String?
)
