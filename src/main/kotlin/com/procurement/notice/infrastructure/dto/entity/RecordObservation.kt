package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.infrastructure.dto.entity.tender.RecordDimensions

data class RecordObservation(

    @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("notes") @param:JsonProperty("notes") val notes: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("measure") @param:JsonProperty("measure") val measure: Any?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("unit") @param:JsonProperty("unit") val unit: RecordObservationUnit?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("dimensions") @param:JsonProperty("dimensions") val dimensions: RecordDimensions?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("relatedRequirementId") @param:JsonProperty("relatedRequirementId") val relatedRequirementId: String?

)