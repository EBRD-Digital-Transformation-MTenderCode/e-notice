package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("type", "value", "description", "methodOfPayment")
data class ParticipationFee(

        @JsonProperty("type")
        val type: List<ParticipationFeeType>?,

        @JsonProperty("value")
        val value: Value?,

        @JsonProperty("description")
        val description: String?,

        @JsonProperty("methodOfPayment")
        val methodOfPayment: List<String>?
)