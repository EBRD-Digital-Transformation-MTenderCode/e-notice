package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import java.math.BigDecimal

data class Item @JsonCreator constructor(

        val id: String?,

        val description: String?,

        val classification: Classification?,

        val additionalClassifications: HashSet<Classification>?,

        val quantity: BigDecimal?,

        val unit: Unit?,

        val relatedLot: String?
)