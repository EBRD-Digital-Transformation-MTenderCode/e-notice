package com.procurement.notice.application.service.tender.cancel

import java.time.LocalDateTime

data class CancelStandStillPeriodData(
    val standstillPeriod: StandstillPeriod,
    val amendments: List<Amendment>,
    val tender: Tender
) {
    data class StandstillPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime
    )

    data class Amendment(
        val rationale: String,
        val description: String?,
        val documents: List<Document>?
    ) {

        data class Document(
            val documentType: String,
            val id: String,
            val title: String,
            val description: String?,
            val datePublished: LocalDateTime,
            val url: String
        )
    }

    data class Tender(
        val statusDetails: String
    )
}
