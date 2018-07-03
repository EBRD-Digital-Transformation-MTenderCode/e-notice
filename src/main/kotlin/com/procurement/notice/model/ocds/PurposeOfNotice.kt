package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class PurposeOfNotice @JsonCreator constructor(

        @get:JsonProperty("isACallForCompetition")
        var isACallForCompetition: Boolean?
)