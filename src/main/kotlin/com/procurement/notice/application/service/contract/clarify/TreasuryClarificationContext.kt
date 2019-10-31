package com.procurement.notice.application.service.contract.clarify

import java.time.LocalDateTime

data class TreasuryClarificationContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime
)