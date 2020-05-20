package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordParticipationFee(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("type") @param:JsonProperty("type") val type: List<String> = emptyList(),

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: Value?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("methodOfPayment") @param:JsonProperty("methodOfPayment") val methodOfPayment: List<String> = emptyList()
)
