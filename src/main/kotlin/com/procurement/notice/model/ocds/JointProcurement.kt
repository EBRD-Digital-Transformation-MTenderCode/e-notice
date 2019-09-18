package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class JointProcurement @JsonCreator constructor(

    @get:JsonProperty("isJointProcurement")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val isJointProcurement: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val country: String?
)
