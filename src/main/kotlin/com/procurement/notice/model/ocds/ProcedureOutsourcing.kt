package com.procurement.notice.model.ocds

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty

data class ProcedureOutsourcing @JsonCreator constructor(

        @get:JsonProperty("procedureOutsourced")
        val procedureOutsourced: Boolean?,

        val outsourcedTo: Organization?
)