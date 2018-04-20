package com.procurement.notice.model.entity

import java.util.*

data class BudgetEntity(

        var cpId: String,

        var ocId: String,

        var releaseDate: Date,

        var releaseId: String,

        var stage: String,

        var amount: Double? = 0.00,

        var jsonData: String
)


