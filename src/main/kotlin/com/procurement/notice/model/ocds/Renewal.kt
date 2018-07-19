package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class Renewal @JsonCreator constructor(

        @get:JsonProperty("hasRenewals")
        val hasRenewals: Boolean?,

        val maxNumber: Int?,

        val renewalConditions: String?
)

