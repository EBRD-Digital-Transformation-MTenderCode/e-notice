package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("isAcceleratedProcedure", "acceleratedProcedureJustification")
data class AcceleratedProcedure(

        @JsonProperty("isAcceleratedProcedure")
        val isAcceleratedProcedure: Boolean?,

        @JsonProperty("acceleratedProcedureJustification")
        val acceleratedProcedureJustification: String?
)

