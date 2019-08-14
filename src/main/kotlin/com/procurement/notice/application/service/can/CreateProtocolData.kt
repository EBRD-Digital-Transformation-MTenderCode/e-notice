package com.procurement.notice.application.service.can

import java.time.LocalDateTime
import java.util.*

data class CreateProtocolData(
    val can: CAN,
    val bids: List<Bid>?,
    val lot: Lot
) {
    data class CAN(
        val id: UUID,
        val lotId: UUID,
        val awardId: UUID?,
        val date: LocalDateTime,
        val status: String,
        val statusDetails: String
    )

    data class Bid(
        val id: UUID,
        val statusDetails: String
    )

    data class Lot(
        val id: UUID,
        val statusDetails: String
    )
}
