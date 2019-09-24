package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator

data class VerificationDto @JsonCreator constructor(

    val contract: ContractFerification
)

data class ContractFerification @JsonCreator constructor(
    var statusDetails: String
)