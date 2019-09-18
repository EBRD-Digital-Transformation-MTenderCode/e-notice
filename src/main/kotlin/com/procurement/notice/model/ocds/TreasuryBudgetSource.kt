package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import java.math.BigDecimal

data class TreasuryBudgetSource @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var budgetBreakdownID: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val budgetIBAN: String?,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amount: BigDecimal?
)
