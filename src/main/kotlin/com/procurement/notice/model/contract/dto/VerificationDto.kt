package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class VerificationDto @JsonCreator constructor(

    val contract: ContractFerification
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractFerification @JsonCreator constructor(
    var statusDetails: String
)