package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BudgetSource @JsonCreator constructor(

    val id: String?,

    @JsonDeserialize(using = AmountDeserializer::class)
    @JsonSerialize(using = AmountSerializer::class)
    val amount: BigDecimal?,

    val currency: String?
)