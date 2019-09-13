package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class LotGroup @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val relatedLots: List<String>?,

    @get:JsonProperty("optionToCombine")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val optionToCombine: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val maximumValue: Value?
)