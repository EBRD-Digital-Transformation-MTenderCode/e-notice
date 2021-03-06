package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Milestone

data class ActivationDto @JsonCreator constructor(

    val contract: ContractActivation,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val lots: List<Lot>,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val cans: List<Can>
)

data class ContractActivation @JsonCreator constructor(

    var status: String,

    var statusDetails: String,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val milestones: List<Milestone>
)
