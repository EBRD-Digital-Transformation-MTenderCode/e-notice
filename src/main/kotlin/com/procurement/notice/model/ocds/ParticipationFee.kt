package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator

data class ParticipationFee @JsonCreator constructor(

        val type: List<ParticipationFeeType>?,

        val value: Value?,

        val description: String?,

        val methodOfPayment: List<String>?
)