package com.procurement.notice.application.service.cn

import java.time.LocalDateTime

data class UpdateCNContext(
    val cpid: String,
    val ocid: String,
    val stage: String,
    val releaseDate: LocalDateTime,
    val isAuction: Boolean
)
