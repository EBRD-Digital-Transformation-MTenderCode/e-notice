package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.model.ocds.QualificationSystemMethod
import com.procurement.notice.model.ocds.ReductionCriteria

data class RecordOtherCriteria(
    @JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("reductionCriteria") @param:JsonProperty("reductionCriteria") val reductionCriteria: ReductionCriteria? = null,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("qualificationSystemMethods") @param:JsonProperty("qualificationSystemMethods") val qualificationSystemMethods: List<QualificationSystemMethod> = emptyList()
)
