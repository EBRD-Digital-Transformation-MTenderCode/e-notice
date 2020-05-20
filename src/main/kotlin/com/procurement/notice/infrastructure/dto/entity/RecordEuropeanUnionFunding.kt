package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

data class RecordEuropeanUnionFunding(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("projectIdentifier") @param:JsonProperty("projectIdentifier") val projectIdentifier: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("projectName") @param:JsonProperty("projectName") val projectName: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
)
