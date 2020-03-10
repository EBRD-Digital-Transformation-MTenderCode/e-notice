package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.access.infrastructure.bind.quantity.QuantitySerializer
import com.procurement.notice.infrastructure.bind.quantity.QuantityDeserializer
import com.procurement.notice.infrastructure.dto.entity.RecordClassification
import com.procurement.notice.infrastructure.dto.entity.address.RecordAddress
import java.math.BigDecimal

data class RecordItem (

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("internalId") @param:JsonProperty("internalId") val internalId: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("classification") @param:JsonProperty("classification") val classification: RecordClassification?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("additionalClassifications") @param:JsonProperty("additionalClassifications") val additionalClassifications: List<RecordClassification> = emptyList(),

    @param:JsonDeserialize(using = QuantityDeserializer::class)
    @field:JsonSerialize(using = QuantitySerializer::class)
    @field:JsonProperty("quantity") @param:JsonProperty("quantity") val quantity: BigDecimal?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: RecordUnit?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("deliveryAddress") @param:JsonProperty("deliveryAddress") val deliveryAddress: RecordAddress?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedLot") @param:JsonProperty("relatedLot") val relatedLot: String?
)
