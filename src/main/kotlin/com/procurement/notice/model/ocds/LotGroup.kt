package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class LotGroup @JsonCreator constructor(

        val id: String?,

        val relatedLots: List<String>?,

        @get:JsonProperty("optionToCombine")
        val optionToCombine: Boolean?,

        val maximumValue: Value?
)