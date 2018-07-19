package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

@JsonInclude(JsonInclude.Include.NON_NULL)
data class RecurrentProcurement @JsonCreator constructor(

        @get:JsonProperty("isRecurrent")
        val isRecurrent: Boolean?,

        val dates: List<Period>?,

        val description: String?
)