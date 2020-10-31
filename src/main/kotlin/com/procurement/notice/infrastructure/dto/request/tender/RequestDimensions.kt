package com.procurement.notice.infrastructure.dto.request.tender

import com.fasterxml.jackson.annotation.JsonProperty

data class RequestDimensions(
    @field:JsonProperty("requirementClassIdPR") @param:JsonProperty("requirementClassIdPR") val requirementClassIdPR: String
)