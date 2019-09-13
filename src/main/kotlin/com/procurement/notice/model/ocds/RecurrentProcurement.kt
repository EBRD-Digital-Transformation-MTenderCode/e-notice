package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecurrentProcurement @JsonCreator constructor(

    @get:JsonProperty("isRecurrent")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val isRecurrent: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val dates: List<Period>?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val description: String?
)