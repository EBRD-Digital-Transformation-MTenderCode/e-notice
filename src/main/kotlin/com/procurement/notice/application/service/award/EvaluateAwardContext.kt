package com.procurement.notice.application.service.award

import java.time.LocalDateTime

data class EvaluateAwardContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime
)
