package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.procurement.notice.model.ocds.Contract

data class SigningDto @JsonCreator constructor(

    val contract: Contract
)
