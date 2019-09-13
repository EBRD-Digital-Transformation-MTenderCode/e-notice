package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class ProcedureOutsourcing @JsonCreator constructor(

    @get:JsonProperty("procedureOutsourced")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    val procedureOutsourced: Boolean?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val outsourcedTo: Organization?
)