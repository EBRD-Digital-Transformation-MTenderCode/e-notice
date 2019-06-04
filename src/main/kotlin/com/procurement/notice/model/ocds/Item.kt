package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.access.infrastructure.bind.quantity.QuantitySerializer
import com.procurement.notice.infrastructure.bind.quantity.QuantityDeserializer
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Item @JsonCreator constructor(

    val id: String?,

    val description: String?,

    val classification: Classification?,

    val additionalClassifications: HashSet<Classification>?,

    @JsonDeserialize(using = QuantityDeserializer::class)
    @JsonSerialize(using = QuantitySerializer::class)
    val quantity: BigDecimal?,

    val unit: Unit?,

    val deliveryAddress: Address?,

    val relatedLot: String?
)