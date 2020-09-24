package com.procurement.notice.application.service.fe.create

import java.time.LocalDateTime

data class CreateFeContext(
    val cpid: String,
    val ocid: String,
    val ocidCn: String,
    val stage: String,
    val releaseDate: LocalDateTime,
    val startDate: LocalDateTime
)
