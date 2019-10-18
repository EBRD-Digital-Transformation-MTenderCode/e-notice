package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.coefficient.value.CoefficientValueDeserializer
import com.procurement.notice.infrastructure.bind.coefficient.value.CoefficientValueSerializer
import java.math.BigDecimal

data class Coefficient(
    val id: String,

    @JsonDeserialize(using = CoefficientValueDeserializer::class)
    @JsonSerialize(using = CoefficientValueSerializer::class)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: CoefficientValue,

    @field:JsonProperty("coefficient") @param:JsonProperty("coefficient") val coefficient: BigDecimal
)