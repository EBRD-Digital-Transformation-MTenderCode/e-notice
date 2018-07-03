package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class RecurrentProcurement @JsonCreator constructor(

        @get:JsonProperty("isRecurrent")
        val isRecurrent: Boolean?,

        val dates: List<Period>?,

        val description: String?
)