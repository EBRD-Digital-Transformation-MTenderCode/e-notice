package com.procurement.notice.application.service.tender.cancel

data class CancelledStandStillPeriodData(
    val cpid: String,
    val ocid: String,
    val amendmentsIds: List<String>
)