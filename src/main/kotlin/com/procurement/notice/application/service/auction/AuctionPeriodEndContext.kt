package com.procurement.notice.application.service.auction

import java.time.LocalDateTime

data class AuctionPeriodEndContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime
)
