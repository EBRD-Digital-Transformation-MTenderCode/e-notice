package com.procurement.notice.application.service.can

import java.time.LocalDateTime

data class CreateCANContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime,
    val startDate: LocalDateTime
)