package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.domain.model.money.Money
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import java.math.BigDecimal

data class Value @JsonCreator constructor(

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var amount: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    var currency: String?,

    @param:JsonDeserialize(using = AmountDeserializer::class)
    @field:JsonSerialize(using = AmountSerializer::class)
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val amountNet: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val valueAddedTaxIncluded: Boolean?
)

fun Money.toValue(amountNet: BigDecimal? = null, valueAddedTaxIncluded: Boolean? = null): Value = this.let { money ->
    Value(
        amount = money.amount,
        currency = money.currency,
        amountNet = amountNet,
        valueAddedTaxIncluded = valueAddedTaxIncluded
    )
}
