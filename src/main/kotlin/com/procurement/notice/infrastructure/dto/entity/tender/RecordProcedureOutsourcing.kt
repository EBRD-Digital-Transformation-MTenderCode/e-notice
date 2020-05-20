package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.parties.RecordOrganization

data class RecordProcedureOutsourcing(

    @get:JsonInclude(JsonInclude.Include.NON_NULL)
    @get:JsonProperty("procedureOutsourced") @param:JsonProperty("procedureOutsourced") val procedureOutsourced: Boolean?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("outsourcedTo") @param:JsonProperty("outsourcedTo") val outsourcedTo: RecordOrganization?
)
