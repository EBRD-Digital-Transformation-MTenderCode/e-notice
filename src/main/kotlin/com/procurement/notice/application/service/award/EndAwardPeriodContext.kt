package com.procurement.notice.application.service.award

import com.procurement.notice.domain.model.ProcurementMethod
import java.time.LocalDateTime

data class EndAwardPeriodContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val pmd: ProcurementMethod,
    val releaseDate: LocalDateTime
)
