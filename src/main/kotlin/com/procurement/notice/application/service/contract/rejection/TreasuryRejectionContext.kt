package com.procurement.notice.application.service.contract.rejection

import com.procurement.notice.domain.model.ProcurementMethod
import java.time.LocalDateTime

data class TreasuryRejectionContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val pmd: ProcurementMethod,
    val releaseDate: LocalDateTime
)
