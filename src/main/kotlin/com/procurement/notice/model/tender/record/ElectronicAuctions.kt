package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value
import java.time.LocalDateTime

data class ElectronicAuctions @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val details: Set<ElectronicAuctionsDetails>
)

data class ElectronicAuctionsDetails @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedLot: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val auctionPeriod: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val electronicAuctionModalities: Set<ElectronicAuctionModalities>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val electronicAuctionResult: Set<ElectronicAuctionResult>?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val electronicAuctionProgress: Set<ElectronicAuctionProgress>?
)

data class ElectronicAuctionModalities @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val url: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val eligibleMinimumDifference: Value?
)

data class ElectronicAuctionResult @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedBid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?
)

data class ElectronicAuctionProgress @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val id: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val period: Period?,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    val breakdown: Set<ElectronicAuctionProgressBreakdown>
)

data class ElectronicAuctionProgressBreakdown @JsonCreator constructor(

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val relatedBid: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val status: String?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val dateMet: LocalDateTime?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    val value: Value?
)
