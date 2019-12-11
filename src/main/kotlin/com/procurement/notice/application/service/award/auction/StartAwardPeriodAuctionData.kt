package com.procurement.notice.application.service.award.auction

import com.procurement.access.domain.model.enums.LotStatus
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.enums.AwardStatus
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.domain.model.lot.LotId
import com.procurement.notice.domain.model.money.Money
import java.time.LocalDateTime

data class StartAwardPeriodAuctionData(

    val awards: List<Award>,
    val unsuccessfulLots: List<UnsuccessfulLot>,

    val tenderStatusDetails: TenderStatusDetails,
    val electronicAuctions: ElectronicAuctions
) {
    data class Award(
        val id: AwardId,
        val title: String,
        val description: String,
        val date: LocalDateTime,
        val status: AwardStatus,
        val statusDetails: AwardStatusDetails,
        val relatedLots: List<LotId>
    )

    data class UnsuccessfulLot(
        val id: LotId,
        val status: LotStatus
    )

    data class Tender(
        val statusDetails: TenderStatusDetails
    )

    data class ElectronicAuctions(
        val details: List<Detail>
    ) {
        data class Detail(
            val id: String,
            val relatedLot: LotId,
            val auctionPeriod: AuctionPeriod,
            val electronicAuctionModalities: List<ElectronicAuctionModality>
        ) {
            data class AuctionPeriod(
                val startDate: LocalDateTime
            )

            data class ElectronicAuctionModality(
                val url: String,
                val eligibleMinimumDifference: Money
            )
        }
    }
}
