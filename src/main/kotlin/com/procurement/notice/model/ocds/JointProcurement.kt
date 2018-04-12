package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isJointProcurement", "country")
data class JointProcurement(

        @JsonProperty("isJointProcurement")
        val isJointProcurement: Boolean?,

        @JsonProperty("country")
        val country: String?
)
