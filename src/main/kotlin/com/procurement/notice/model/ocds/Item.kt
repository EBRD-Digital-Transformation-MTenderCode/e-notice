package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Item @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val classification: Classification?,

        val additionalClassifications: HashSet<Classification>?,

        val quantity: BigDecimal?,

        val unit: Unit?,

        val deliveryAddress: Address?,

        val relatedLot: String?
)