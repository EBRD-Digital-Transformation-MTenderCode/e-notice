package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("reducesTimeLimits", "isACallForCompetition", "socialOrOtherSpecificServices")
data class PurposeOfNotice(

        @JsonProperty("reducesTimeLimits")
        val reducesTimeLimits: Boolean?,

        @JsonProperty("isACallForCompetition")
        var isACallForCompetition: Boolean?,

        @JsonProperty("socialOrOtherSpecificServices")
        val socialOrOtherSpecificServices: Boolean?
)