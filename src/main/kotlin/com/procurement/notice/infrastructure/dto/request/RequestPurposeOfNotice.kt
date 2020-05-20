package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestPurposeOfNotice(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isACallForCompetition") @param:JsonProperty("isACallForCompetition") val isACallForCompetition: Boolean?

)

