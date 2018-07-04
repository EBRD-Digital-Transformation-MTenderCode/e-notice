package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

@JsonInclude(JsonInclude.Include.NON_NULL)
data class EiBudget @JsonCreator constructor(

        val id: String?,

        val period: Period?,

        val amount: Value?
)
