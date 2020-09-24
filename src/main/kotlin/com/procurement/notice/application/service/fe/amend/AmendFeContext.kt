package com.procurement.notice.application.service.fe.amend

import java.time.LocalDateTime

data class AmendFeContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime,
    val startDate: LocalDateTime
)