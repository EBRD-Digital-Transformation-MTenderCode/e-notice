package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.contract.Can
import com.procurement.notice.model.ocds.*

data class ActivationDto @JsonCreator constructor(

        val contract: ContractActivation,

        val lots: HashSet<Lot>,

        val cans: HashSet<Can>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractActivation @JsonCreator constructor(

    var status: String,

    var statusDetails: String,

    val milestones: List<Milestone>
)

