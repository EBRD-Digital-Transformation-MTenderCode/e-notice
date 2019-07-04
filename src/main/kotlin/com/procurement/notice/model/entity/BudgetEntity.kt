package com.procurement.notice.model.entity

import com.fasterxml.jackson.annotation.JsonInclude
import java.math.BigDecimal
import java.util.*

@JsonInclude(JsonInclude.Include.NON_NULL)
data class BudgetEntity(

        var cpId: String,

        var ocId: String,

        var publishDate: Date,

        var releaseDate: Date,

        var releaseId: String,

        var stage: String,

        var amount: BigDecimal? = BigDecimal.valueOf(0.00),

        var jsonData: String

)


