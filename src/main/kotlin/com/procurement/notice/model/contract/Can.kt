package com.procurement.notice.model.contract

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Contract

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Can @JsonCreator constructor(

        val contract: Contract
)
