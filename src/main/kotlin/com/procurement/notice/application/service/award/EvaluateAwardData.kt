package com.procurement.notice.application.service.award

import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.bid.BidId
import com.procurement.notice.domain.model.enums.AwardStatus
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.BidDocumentType
import com.procurement.notice.domain.model.lot.LotId
import com.procurement.notice.domain.model.money.Money
import java.math.BigDecimal
import java.time.LocalDateTime

class EvaluateAwardData(
    val award: Award,
    val nextAwardForUpdate: NextAwardForUpdate?,
    val bid: Bid?
) {
    data class Award(
        val id: AwardId,
        val date: LocalDateTime,
        val description: String?,
        val status: AwardStatus,
        val statusDetails: AwardStatusDetails,
        val relatedLots: List<LotId>,
        val relatedBid: BidId?,
        val value: Value,
        val suppliers: List<Supplier>,
        val documents: List<Document>,
        val weightedValue: Money?
    ) {

        data class Value(
            val amount: BigDecimal?,
            val currency: String
        )

        data class Supplier(
            val id: String,
            val name: String
        )

        data class Document(
            val documentType: String,
            val id: String,
            val datePublished: LocalDateTime,
            val url: String,
            val title: String?,
            val description: String?,
            val relatedLots: List<LotId>
        )
    }

    data class NextAwardForUpdate(
        val id: AwardId,
        val statusDetails: AwardStatusDetails
    )

    data class Bid(
        val id: BidId,
        val documents: List<Document>
    ) {
        data class Document(
            val documentType: BidDocumentType,
            val id: String,
            val datePublished: LocalDateTime,
            val url: String,
            val title: String?,
            val description: String?,
            val relatedLots: List<LotId>
        )
    }
}