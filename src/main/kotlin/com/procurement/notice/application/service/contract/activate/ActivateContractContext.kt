package com.procurement.notice.application.service.contract.activate

import com.procurement.notice.domain.model.ProcurementMethod
import java.time.LocalDateTime

data class ActivateContractContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val pmd: ProcurementMethod,
    val releaseDate: LocalDateTime
)
