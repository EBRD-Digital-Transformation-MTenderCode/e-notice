package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import java.math.BigDecimal

data class Value @JsonCreator constructor(

    @JsonDeserialize(using = AmountDeserializer::class)
    @JsonSerialize(using = AmountSerializer::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var amount: BigDecimal?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var currency: String?,

    @JsonDeserialize(using = AmountDeserializer::class)
    @JsonSerialize(using = AmountSerializer::class)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val amountNet: BigDecimal?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val valueAddedTaxIncluded: Boolean?
)
