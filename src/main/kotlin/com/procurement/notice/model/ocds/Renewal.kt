package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class Renewal @JsonCreator constructor(

        @get:JsonProperty("hasRenewals")
        val hasRenewals: Boolean?,

        val maxNumber: Int?,

        val renewalConditions: String?
)

