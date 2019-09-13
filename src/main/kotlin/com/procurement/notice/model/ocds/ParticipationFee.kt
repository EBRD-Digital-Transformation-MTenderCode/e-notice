package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

data class ParticipationFee @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val type: List<String>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val methodOfPayment: List<String>?
)