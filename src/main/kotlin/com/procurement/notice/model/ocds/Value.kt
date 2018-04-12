package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("amount", "currency")
data class Value(

        @JsonProperty("amount")
        var amount: Double?,

        @JsonProperty("currency")
        val currency: Currency?
)
