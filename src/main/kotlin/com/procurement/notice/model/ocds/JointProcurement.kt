package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class JointProcurement @JsonCreator constructor(

        @get:JsonProperty("isJointProcurement")
        val isJointProcurement: Boolean?,

        val country: String?
)
