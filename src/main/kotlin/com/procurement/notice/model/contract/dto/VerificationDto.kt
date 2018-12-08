package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class VerificationDto @JsonCreator constructor(

        val contract: ContractVerification
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractVerification @JsonCreator constructor(

        var statusDetails: String
)