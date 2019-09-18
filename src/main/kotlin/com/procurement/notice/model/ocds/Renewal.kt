package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class Renewal @JsonCreator constructor(

    @get:JsonProperty("hasRenewals")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val hasRenewals: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val maxNumber: Int?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val renewalConditions: String?
)
