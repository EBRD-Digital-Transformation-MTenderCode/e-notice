package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class PurposeOfNotice @JsonCreator constructor(

    @get:JsonProperty("isACallForCompetition")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    var isACallForCompetition: Boolean?
)