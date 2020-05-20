package com.procurement.notice.application.service.award

import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.bid.BidId
import com.procurement.notice.domain.model.document.DocumentId
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.BidDocumentType
import com.procurement.notice.domain.model.lot.LotId
import java.time.LocalDateTime

data class AwardConsiderationData(
    val award: Award,
    val bid: Bid
) {
    data class Award(
        val id: AwardId,
        val statusDetails: AwardStatusDetails
    )

    data class Bid(
        val documents: List<Document>,
        val id: BidId
    ) {
        data class Document(
            val documentType: BidDocumentType,
            val id: DocumentId,
            val title: String?,
            val description: String?,
            val datePublished: LocalDateTime,
            val url: String?,
            val relatedLots: List<LotId>
        )
    }
}