package com.procurement.notice.application.service.tender.cancel

import java.time.LocalDateTime

data class CancelStandStillPeriodContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime
)
