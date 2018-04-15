package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("reducesTimeLimits", "isACallForCompetition", "socialOrOtherSpecificServices")
data class PurposeOfNotice(

        @JsonProperty("reducesTimeLimits")
        @get:JsonProperty("reducesTimeLimits")
        val reducesTimeLimits: Boolean?,

        @JsonProperty("isACallForCompetition")
        @get:JsonProperty("isACallForCompetition")
        var isACallForCompetition: Boolean?,

        @JsonProperty("socialOrOtherSpecificServices")
        @get:JsonProperty("socialOrOtherSpecificServices")
        val socialOrOtherSpecificServices: Boolean?
)