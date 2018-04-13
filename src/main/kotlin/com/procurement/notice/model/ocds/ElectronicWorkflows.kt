package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("useOrdering", "usePayment", "acceptInvoicing")
data class ElectronicWorkflows(

        @JsonProperty("useOrdering")
        val useOrdering: Boolean?,

        @JsonProperty("usePayment")
        val usePayment: Boolean?,

        @JsonProperty("acceptInvoicing")
        val acceptInvoicing: Boolean?
)