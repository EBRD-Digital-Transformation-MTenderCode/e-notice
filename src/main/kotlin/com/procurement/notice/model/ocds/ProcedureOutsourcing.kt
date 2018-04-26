package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonPropertyOrder

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder("procedureOutsourced", "outsourcedTo")
data class ProcedureOutsourcing(

        @JsonProperty("procedureOutsourced")
        @get:JsonProperty("procedureOutsourced")
        val procedureOutsourced: Boolean?,

        @JsonProperty("outsourcedTo")
        val outsourcedTo: Organization?
)