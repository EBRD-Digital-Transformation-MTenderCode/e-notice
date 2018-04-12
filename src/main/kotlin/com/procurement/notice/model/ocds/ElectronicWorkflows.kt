package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("useOrdering", "usePayment", "acceptInvoicing")
data class ElectronicWorkflows(

        @JsonProperty("useOrdering")
        val useOrdering: Boolean?,

        @JsonProperty("usePayment")
        val usePayment: Boolean?,

        @JsonProperty("acceptInvoicing")
        val acceptInvoicing: Boolean?
)