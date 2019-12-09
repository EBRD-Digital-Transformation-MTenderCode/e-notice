package com.procurement.notice.application.service.award

import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.enums.AwardStatusDetails

data class AwardConsiderationData(
    val award: Award
) {
    data class Award(
        val id: AwardId,
        val statusDetails: AwardStatusDetails
    )
}