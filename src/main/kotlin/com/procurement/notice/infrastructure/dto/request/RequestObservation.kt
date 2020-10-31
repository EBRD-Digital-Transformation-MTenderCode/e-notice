package com.procurement.notice.infrastructure.dto.request

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.request.tender.RequestDimensions

data class RequestObservation(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("notes") @param:JsonProperty("notes") val notes: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("measure") @param:JsonProperty("measure") val measure: Any?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: RequestObservationUnit?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RequestPeriod?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("dimensions") @param:JsonProperty("dimensions") val dimensions: RequestDimensions?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedRequirementId") @param:JsonProperty("relatedRequirementId") val relatedRequirementId: String?
)