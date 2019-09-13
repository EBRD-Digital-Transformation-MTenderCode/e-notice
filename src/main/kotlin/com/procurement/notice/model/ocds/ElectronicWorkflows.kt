package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ElectronicWorkflows @JsonCreator constructor(

    @get:JsonProperty("useOrdering")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val useOrdering: Boolean?,

    @get:JsonProperty("usePayment")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val usePayment: Boolean?,

    @get:JsonProperty("acceptInvoicing")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val acceptInvoicing: Boolean?
)