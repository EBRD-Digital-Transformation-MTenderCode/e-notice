package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecurrentProcurement @JsonCreator constructor(

    @get:JsonProperty("isRecurrent")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val isRecurrent: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val dates: List<Period>?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?
)
