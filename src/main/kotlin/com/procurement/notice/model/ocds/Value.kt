package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("amount", "currency")
data class Value(

        @JsonProperty("amount")
        var amount: Double?,

        @JsonProperty("currency")
        val currency: Currency?
)
