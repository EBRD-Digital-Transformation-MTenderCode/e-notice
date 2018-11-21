package com.procurement.notice.model.contract.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import java.time.LocalDateTime

data class IssuingAcDto @JsonCreator constructor(

        val contract: ContractIssuingAc
)


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ContractIssuingAc @JsonCreator constructor(
    var date: LocalDateTime,
    var statusDetails: String
)