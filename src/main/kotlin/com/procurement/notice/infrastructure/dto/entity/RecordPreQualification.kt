package com.procurement.notice.infrastructure.dto.entity

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty

class RecordPreQualification(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("period") @param:JsonProperty("period") val period: RecordPeriod?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("qualificationPeriod") @param:JsonProperty("qualificationPeriod") val qualificationPeriod: RecordPeriod?
)
