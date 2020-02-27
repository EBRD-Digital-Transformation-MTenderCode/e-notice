package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RequestJointProcurement(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isJointProcurement") @param:JsonProperty("isJointProcurement") val isJointProcurement: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("country") @param:JsonProperty("country") val country: String?
)
