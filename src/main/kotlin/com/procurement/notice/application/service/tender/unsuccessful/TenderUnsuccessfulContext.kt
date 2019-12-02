package com.procurement.notice.application.service.tender.unsuccessful

import java.time.LocalDateTime

data class TenderUnsuccessfulContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime
)
