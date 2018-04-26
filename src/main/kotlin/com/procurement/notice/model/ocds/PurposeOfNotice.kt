package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("reducesTimeLimits", "isACallForCompetition", "socialOrOtherSpecificServices")
data class PurposeOfNotice(

        @JsonProperty("isACallForCompetition")
        @get:JsonProperty("isACallForCompetition")
        var isACallForCompetition: Boolean?
)