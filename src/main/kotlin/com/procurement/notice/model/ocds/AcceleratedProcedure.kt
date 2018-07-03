package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty


data class AcceleratedProcedure @JsonCreator constructor(

        @get:JsonProperty("isAcceleratedProcedure")
        val isAcceleratedProcedure: Boolean?,

        val acceleratedProcedureJustification: String?
)

