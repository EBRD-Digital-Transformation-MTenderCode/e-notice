package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.award.ConsiderAwardData
import com.procurement.notice.infrastructure.dto.award.ConsiderAwardRequest

fun ConsiderAwardRequest.convert(): ConsiderAwardData = ConsiderAwardData(
    award = this.award.let { award -> ConsiderAwardData.Award(
        id = award.id,
        statusDetails = award.statusDetails
    ) }
)