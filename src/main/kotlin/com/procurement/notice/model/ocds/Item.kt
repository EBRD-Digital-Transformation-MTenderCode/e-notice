package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.access.infrastructure.bind.quantity.QuantitySerializer
import com.procurement.notice.infrastructure.bind.quantity.QuantityDeserializer
import java.math.BigDecimal

data class Item @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val classification: Classification?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val additionalClassifications: HashSet<Classification>?,

    @JsonDeserialize(using = QuantityDeserializer::class)
    @JsonSerialize(using = QuantitySerializer::class)
    val quantity: BigDecimal?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val unit: Unit?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val deliveryAddress: Address?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?
)