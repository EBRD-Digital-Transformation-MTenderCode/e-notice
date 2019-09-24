package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class LotGroup @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @get:JsonProperty("optionToCombine")
    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    val optionToCombine: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val maximumValue: Value?
)
