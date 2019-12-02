package com.procurement.notice.application.service.tender.periodEnd

import java.time.LocalDateTime

data class TenderPeriodEndContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime
)
