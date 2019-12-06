package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.award.ConsiderAwardData
import com.procurement.notice.infrastructure.dto.award.AwardConsiderationRequest

fun AwardConsiderationRequest.convert(): ConsiderAwardData = ConsiderAwardData(
    award = this.award.let { award -> ConsiderAwardData.Award(
        id = award.id,
        statusDetails = award.statusDetails
    ) }
)