package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

data class EiBudget @JsonCreator constructor(

        val id: String?,

        val period: Period?,

        val amount: Value?
)
