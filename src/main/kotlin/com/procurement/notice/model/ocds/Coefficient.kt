package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.application.model.CoefficientRate
import com.procurement.notice.infrastructure.bind.coefficient.CoefficientRateDeserializer
import com.procurement.notice.infrastructure.bind.coefficient.CoefficientRateSerializer
import com.procurement.notice.infrastructure.bind.coefficient.value.CoefficientValueDeserializer
import com.procurement.notice.infrastructure.bind.coefficient.value.CoefficientValueSerializer

data class Coefficient(
    val id: String,

    @JsonDeserialize(using = CoefficientValueDeserializer::class)
    @JsonSerialize(using = CoefficientValueSerializer::class)
    @field:JsonProperty("value") @param:JsonProperty("value") val value: CoefficientValue,

    @JsonDeserialize(using = CoefficientRateDeserializer::class)
    @JsonSerialize(using = CoefficientRateSerializer::class)
    @field:JsonProperty("coefficient") @param:JsonProperty("coefficient") val coefficient: CoefficientRate
)