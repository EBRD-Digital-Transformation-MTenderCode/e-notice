package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonPropertyOrder("procedureOutsourced", "outsourcedTo")
data class ProcedureOutsourcing(

        @JsonProperty("procedureOutsourced")
        val procedureOutsourced: Boolean?,

        @JsonProperty("outsourcedTo")
        val outsourcedTo: Organization?
)