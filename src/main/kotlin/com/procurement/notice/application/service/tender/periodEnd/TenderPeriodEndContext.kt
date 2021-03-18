package com.procurement.notice.application.service.tender.periodEnd

import com.procurement.notice.domain.model.ProcurementMethod
import java.time.LocalDateTime

data class TenderPeriodEndContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime,
    val pmd: ProcurementMethod
)
