package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.award.auction.StartAwardPeriodAuctionData
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.award.auction.StartAwardPeriodAuctionRequest
import com.procurement.notice.lib.errorIfEmpty
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.lib.orThrow

fun StartAwardPeriodAuctionRequest.convert(): StartAwardPeriodAuctionData = StartAwardPeriodAuctionData(
    awards = this.awards
        .errorIfEmpty {
            ErrorException(
                error = ErrorType.IS_EMPTY,
                message = "Request contains empty list of awards."
            )
        }
        ?.map { award ->
            StartAwardPeriodAuctionData.Award(
                id = award.id,
                status = award.status,
                statusDetails = award.statusDetails,
                title = award.title,
                description = award.description,
                date = award.date,
                relatedLots = award.relatedLots.toList()
            )
        }.orEmpty(),
    electronicAuctions = this.electronicAuctions.let { electronicAuctions ->
        StartAwardPeriodAuctionData.ElectronicAuctions(
            details = electronicAuctions.details
                .mapIfNotEmpty { detail ->
                    StartAwardPeriodAuctionData.ElectronicAuctions.Detail(
                        id = detail.id,
                        auctionPeriod = detail.auctionPeriod.let { auctionPeriod ->
                            StartAwardPeriodAuctionData.ElectronicAuctions.Detail.AuctionPeriod(
                                startDate = auctionPeriod.startDate
                            )
                        },
                        electronicAuctionModalities = detail.electronicAuctionModalities
                            .mapIfNotEmpty { electronicAuctionModality ->
                            StartAwardPeriodAuctionData.ElectronicAuctions.Detail.ElectronicAuctionModality(
                                url = electronicAuctionModality.url,
                                eligibleMinimumDifference = electronicAuctionModality.eligibleMinimumDifference
                            )
                        }.orThrow {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The electronicAuctionModalities list is empty."
                                )
                            },
                        relatedLot = detail.relatedLot
                    )
                }
                .orThrow {
                    ErrorException(
                        error = ErrorType.IS_EMPTY,
                        message = "The details list in electronicAuctions is empty."
                    )
                }
            )
    },
    tenderStatusDetails = this.tenderStatusDetails,
    unsuccessfulLots = this.unsuccessfulLots
        .errorIfEmpty {
            ErrorException(
                error = ErrorType.IS_EMPTY,
                message = "Request contains empty list of unsuccessful lots."
            )
        }
        ?.map { unsuccessfulLot ->
            StartAwardPeriodAuctionData.UnsuccessfulLot(
                id = unsuccessfulLot.id,
                status = unsuccessfulLot.status
            )
        }.orEmpty()
)