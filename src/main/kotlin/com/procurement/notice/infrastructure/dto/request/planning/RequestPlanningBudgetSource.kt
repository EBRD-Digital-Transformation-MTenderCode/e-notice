package com.procurement.notice.infrastructure.dto.request.planning

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import java.math.BigDecimal

data class RequestPlanningBudgetSource(

    @field:JsonProperty("budgetBreakdownID") @param:JsonProperty("budgetBreakdownID") val budgetBreakdownID: String,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String?
)