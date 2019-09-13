package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value
import java.time.LocalDateTime

data class ElectronicAuctions @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val details: Set<ElectronicAuctionsDetails>
)

data class ElectronicAuctionsDetails @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val auctionPeriod: Period?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val electronicAuctionModalities: Set<ElectronicAuctionModalities>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val electronicAuctionResult: Set<ElectronicAuctionResult>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val electronicAuctionProgress: Set<ElectronicAuctionProgress>?
)

data class ElectronicAuctionModalities @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val url: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val eligibleMinimumDifference: Value?
)

data class ElectronicAuctionResult @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedBid: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?
)

data class ElectronicAuctionProgress @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    val breakdown: Set<ElectronicAuctionProgressBreakdown>
)

data class ElectronicAuctionProgressBreakdown @JsonCreator constructor(

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedBid: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val status: String?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val dateMet: LocalDateTime?,

    @JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?
)