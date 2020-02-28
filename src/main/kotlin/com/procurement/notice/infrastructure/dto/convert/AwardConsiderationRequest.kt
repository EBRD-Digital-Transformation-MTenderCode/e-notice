package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.award.AwardConsiderationData
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.award.AwardConsiderationRequest
import com.procurement.notice.lib.errorIfEmpty

fun AwardConsiderationRequest.convert(): AwardConsiderationData = AwardConsiderationData(
    award = this.award
        .let { award ->
            AwardConsiderationData.Award(
                id = award.id,
                statusDetails = award.statusDetails
            )
        },
    bid = this.bid
        .let { bid ->
            AwardConsiderationData.Bid(
                id = bid.id,
                documents = bid.documents
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The bid '${bid.id}' contain empty list of the documents. "
                        )
                    }
                    ?.map { document ->
                        AwardConsiderationData.Bid.Document(
                            id = document.id,
                            description = document.description,
                            title = document.title,
                            documentType = document.documentType,
                            relatedLots = document.relatedLots
                                .errorIfEmpty {
                                    ErrorException(
                                        error = ErrorType.IS_EMPTY,
                                        message = "The bids document '${document.id}' contain empty list of relatedLots. "
                                    )
                                }
                                ?.map { it }
                                .orEmpty(),
                            url = document.url,
                            datePublished = document.datePublished
                        )
                    }
                    .orEmpty()
            )
        }
)