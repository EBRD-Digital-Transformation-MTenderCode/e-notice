package com.procurement.notice.model.entity

import java.math.BigDecimal
import java.util.*

data class BudgetEntity(

        var cpId: String,

        var ocId: String,

        var releaseDate: Date,

        var releaseId: String,

        var stage: String,

        var amount: BigDecimal? = BigDecimal.valueOf(0.00),

        var jsonData: String
)


