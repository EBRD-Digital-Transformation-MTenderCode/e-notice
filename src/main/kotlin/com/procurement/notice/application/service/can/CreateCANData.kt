package com.procurement.notice.application.service.can

import java.time.LocalDateTime
import java.util.*

data class CreateCANData(
    val can: CAN
) {
    data class CAN(
        val id: UUID,
        val lotId: UUID,
        val awardId: UUID?,
        val date: LocalDateTime,
        val status: String,
        val statusDetails: String
    )
}
