package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ElectronicWorkflows @JsonCreator constructor(

    @get:JsonProperty("useOrdering")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val useOrdering: Boolean?,

    @get:JsonProperty("usePayment")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val usePayment: Boolean?,

    @get:JsonProperty("acceptInvoicing")
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val acceptInvoicing: Boolean?
)
