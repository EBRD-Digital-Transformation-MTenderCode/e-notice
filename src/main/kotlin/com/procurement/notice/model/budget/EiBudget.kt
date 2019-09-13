package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value

data class EiBudget @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    var amount: Value?
)
