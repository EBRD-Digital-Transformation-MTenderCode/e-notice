package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ElectronicWorkflows @JsonCreator constructor(

        @get:JsonProperty("useOrdering")
        val useOrdering: Boolean?,

        @get:JsonProperty("usePayment")
        val usePayment: Boolean?,

        @get:JsonProperty("acceptInvoicing")
        val acceptInvoicing: Boolean?
)