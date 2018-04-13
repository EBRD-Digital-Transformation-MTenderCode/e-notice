package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
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