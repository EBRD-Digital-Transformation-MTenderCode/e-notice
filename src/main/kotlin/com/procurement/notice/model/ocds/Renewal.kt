package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("hasRenewals", "maxNumber", "renewalConditions")
data class Renewal(

        @JsonProperty("hasRenewals")
        val hasRenewals: Boolean?,

        @JsonProperty("maxNumber")
        val maxNumber: Int?,

        @JsonProperty("renewalConditions")
        val renewalConditions: String?
)

