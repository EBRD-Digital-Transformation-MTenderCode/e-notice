package com.procurement.notice.application.service.award

import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import java.time.LocalDateTime

data class EndAwardPeriodData(
    val contract: Contract?,
    val cans: List<CAN>,
    val tender: Tender,
    val lots: List<Lot>,
    val awards: List<Award>,
    val awardPeriod: AwardPeriod,
    val bids: List<Bid>?
) {

    data class Contract(
        val id: String,
        val status: String,
        val statusDetails: String,
        val milestones: List<Milestone>
    ) {

        data class Milestone(
            val id: String,
            val relatedItems: List<String>?,
            val status: String,
            val additionalInformation: String?,
            val dueDate: LocalDateTime?,
            val title: String,
            val type: String,
            val description: String,
            val dateModified: LocalDateTime?,
            val dateMet: LocalDateTime?,
            val relatedParties: List<RelatedParty>
        ) {

            data class RelatedParty(
                val id: String,
                val name: String
            )
        }
    }

    data class CAN(
        val id: String,
        val status: String,
        val statusDetails: String
    )

    data class Tender(
        val status: TenderStatus,
        val statusDetails: TenderStatusDetails
    )

    data class Lot(
        val id: String,
        val status: String,
        val statusDetails: String
    )

    data class Award(
        val id: String,
        val status: String,
        val statusDetails: String
    )

    data class AwardPeriod(

        val startDate: LocalDateTime,

        val endDate: LocalDateTime
    )

    data class Bid(
        val id: String,
        val status: String,
        val statusDetails: String?
    )
}
