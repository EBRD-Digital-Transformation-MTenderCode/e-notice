package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.award.EvaluateAwardData
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.award.EvaluateAwardRequest
import com.procurement.notice.lib.errorIfEmpty
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.lib.orThrow

fun EvaluateAwardRequest.convert(): EvaluateAwardData = EvaluateAwardData(
    award = this.award
        .let { award ->
            EvaluateAwardData.Award(
                id = award.id,
                date = award.date,
                description = award.description,
                status = award.status,
                statusDetails = award.statusDetails,
                relatedLots = award.relatedLots
                    .mapIfNotEmpty { it }
                    .orThrow {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The award '${award.id}' contains empty list of relatedLots."
                        )
                    },
                value = award.value,
                suppliers = award.suppliers
                    .mapIfNotEmpty { supplier ->
                        EvaluateAwardData.Award.Supplier(
                            id = supplier.id,
                            name = supplier.name
                        )
                    }.orThrow {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The award '${award.id}' contains empty list of suppliers."
                        )
                    },
                documents = award.documents
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The award '${award.id}' contains empty list of documents."
                        )
                    }
                    ?.map { document ->
                        EvaluateAwardData.Award.Document(
                            documentType = document.documentType,
                            id = document.id,
                            datePublished = document.datePublished,
                            url = document.url,
                            title = document.title,
                            description = document.description,
                            relatedLots = document.relatedLots
                                .errorIfEmpty {
                                    ErrorException(
                                        error = ErrorType.IS_EMPTY,
                                        message = "The award '${award.id}' contains empty list of relatedLots in document '${document.id}'."
                                    )
                                }?.toList()
                                .orEmpty()
                        )
                    }.orEmpty(),
                relatedBid = award.relatedBid,
                weightedValue = award.weightedValue
            )
        },
    bid = this.bid
        ?.let { bid ->
            EvaluateAwardData.Bid(
                id = bid.id,
                documents = bid.documents
                    .errorIfEmpty {
                        ErrorException(
                            error = ErrorType.IS_EMPTY,
                            message = "The bid '${bid.id}' contains empty list of documents."
                        )
                    }?.map { document ->
                        EvaluateAwardData.Bid.Document(
                            id = document.id,
                            datePublished = document.datePublished,
                            description = document.description,
                            documentType = document.documentType,
                            relatedLots = document.relatedLots
                                .errorIfEmpty {
                                    ErrorException(
                                        error = ErrorType.IS_EMPTY,
                                        message = "The abid '${bid.id}' contains empty list of relatedLots in document '${document.id}'."
                                    )
                                }?.toList()
                                .orEmpty(),
                            title = document.title,
                            url = document.url
                        )
                    }.orEmpty()
            )
        },
    nextAwardForUpdate = this.nextAwardForUpdate
        ?.let { nextAwardForUpdate ->
            EvaluateAwardData.NextAwardForUpdate(
                id = nextAwardForUpdate.id,
                statusDetails = nextAwardForUpdate.statusDetails
            )
        }
)