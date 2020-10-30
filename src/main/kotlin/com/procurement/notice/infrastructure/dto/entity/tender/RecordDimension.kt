package com.procurement.notice.infrastructure.dto.entity.tender

import com.fasterxml.jackson.annotation.JsonProperty

data class RecordDimension(
    @field:JsonProperty("requirementClassIdPR") @param:JsonProperty("requirementClassIdPR") val requirementClassIdPR: String
)