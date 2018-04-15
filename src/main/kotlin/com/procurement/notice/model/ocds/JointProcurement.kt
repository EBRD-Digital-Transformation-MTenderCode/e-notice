package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("isJointProcurement", "country")
data class JointProcurement(

        @JsonProperty("isJointProcurement")
        @get:JsonProperty("isJointProcurement")
        val isJointProcurement: Boolean?,

        @JsonProperty("country")
        val country: String?
)
