package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordAcceleratedProcedure(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("isAcceleratedProcedure") @param:JsonProperty("isAcceleratedProcedure") val isAcceleratedProcedure: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("acceleratedProcedureJustification") @param:JsonProperty("acceleratedProcedureJustification") val acceleratedProcedureJustification: String?
)
