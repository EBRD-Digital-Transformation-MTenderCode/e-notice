package com.procurement.notice.application.service.can

import java.util.*

data class ConfirmCANData(
    val cans: List<CAN>,
    val lots: List<Lot>,
    val awards: List<Award>,
    val bids: List<Bid>?
) {

    data class CAN(
        val id: UUID,
        val status: String,
        val statusDetails: String
    )

    data class Lot(
        val id: UUID,
        val status: String,
        val statusDetails: String
    )

    data class Award(
        val id: UUID,
        val status: String,
        val statusDetails: String
    )

    data class Bid(
        val id: UUID,
        val status: String,
        val statusDetails: String
    )
}
