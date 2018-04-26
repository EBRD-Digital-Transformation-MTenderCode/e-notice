package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("isAcceleratedProcedure", "acceleratedProcedureJustification")
data class AcceleratedProcedure(

        @JsonProperty("isAcceleratedProcedure")
        @get:JsonProperty("isAcceleratedProcedure")
        val isAcceleratedProcedure: Boolean?,

        @JsonProperty("acceleratedProcedureJustification")
        val acceleratedProcedureJustification: String?
)

