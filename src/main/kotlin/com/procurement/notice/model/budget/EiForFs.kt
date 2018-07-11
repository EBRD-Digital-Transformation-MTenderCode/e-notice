package com.procurement.notice.model.budget

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Value

data class EiForFs @JsonCreator constructor(

        var planning: EiForFsPlanning
)

data class EiForFsPlanning @JsonCreator constructor(

        var budget: EiForFsBudget
)

data class EiForFsBudget @JsonCreator constructor(

        var amount: Value
)
