package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.RecordPeriod

data class RecordRenewalV2(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("minimumRenewals") @param:JsonProperty("minimumRenewals") val minimumRenewals: Int?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("maximumRenewals") @param:JsonProperty("maximumRenewals") val maximumRenewals: Int?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?
)
