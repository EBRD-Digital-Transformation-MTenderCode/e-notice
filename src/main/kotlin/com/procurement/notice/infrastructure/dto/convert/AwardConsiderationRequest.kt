package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.award.AwardConsiderationData
import com.procurement.notice.infrastructure.dto.award.AwardConsiderationRequest

fun AwardConsiderationRequest.convert(): AwardConsiderationData = AwardConsiderationData(
    award = this.award
        .let { award ->
            AwardConsiderationData.Award(
                id = award.id,
                statusDetails = award.statusDetails
            )
        }
)