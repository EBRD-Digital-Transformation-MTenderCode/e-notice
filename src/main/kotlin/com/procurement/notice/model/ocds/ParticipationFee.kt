package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ParticipationFee @JsonCreator constructor(

        val type: List<ParticipationFeeType>?,

        val value: Value?,

        val description: String?,

        val methodOfPayment: List<String>?
)