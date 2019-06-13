package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_EMPTY)
data class ParticipationFee @JsonCreator constructor(

        val type: List<String>?,

        val value: Value?,

        val description: String?,

        val methodOfPayment: List<String>?
)