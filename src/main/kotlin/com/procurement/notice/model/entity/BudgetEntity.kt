package com.procurement.notice.model.entity

import java.util.*

data class BudgetEntity(

        var cpId: String,

        var ocId: String,

        var releaseDate: Date,

        var releaseId: String,

        var stage: String,

        var amount: Double?,

        var jsonData: String
)


