package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Contract

@JsonInclude(JsonInclude.Include.NON_NULL)
data class SigningDto @JsonCreator constructor(
        val contract: Contract
)
