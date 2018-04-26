package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("hasRenewals", "maxNumber", "renewalConditions")
data class Renewal(

        @JsonProperty("hasRenewals")
        @get:JsonProperty("hasRenewals")
        val hasRenewals: Boolean?,

        @JsonProperty("maxNumber")
        val maxNumber: Int?,

        @JsonProperty("renewalConditions")
        val renewalConditions: String?
)

