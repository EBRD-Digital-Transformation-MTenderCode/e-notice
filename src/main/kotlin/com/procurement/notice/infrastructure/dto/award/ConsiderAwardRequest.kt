package com.procurement.notice.infrastructure.dto.award


import com.fasterxml.jackson.annotation.JsonProperty
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.enums.AwardStatusDetails

data class ConsiderAwardRequest(
    @param:JsonProperty("award") @field:JsonProperty("award") val award: Award
) {
    data class Award(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: AwardId,
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: AwardStatusDetails
    )
}