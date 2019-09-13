package com.procurement.notice.model.entity

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.util.*

data class BudgetEntity(

    var cpId: String,

    var ocId: String,

    var publishDate: Date,

    var releaseDate: Date,

    var releaseId: String,

    var stage: String,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var amount: BigDecimal? = BigDecimal.valueOf(0.00),

    var jsonData: String

)


