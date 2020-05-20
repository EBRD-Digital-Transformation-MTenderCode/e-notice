package com.procurement.notice.application.service.contract.clarify

import com.procurement.notice.domain.model.ProcurementMethod
import java.time.LocalDateTime

data class TreasuryClarificationContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val pmd: ProcurementMethod,
    val releaseDate: LocalDateTime
)
