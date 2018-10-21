package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value
import java.time.LocalDateTime


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctions @JsonCreator constructor(

        val details: Set<ElectronicAuctionsDetails>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionsDetails @JsonCreator constructor(

        val id: String?,

        val relatedLot: String?,

        val auctionPeriod: Period?,

        val electronicAuctionModalities: Set<ElectronicAuctionModalities>?,

        val electronicAuctionResult: Set<ElectronicAuctionResult>?,

        val electronicAuctionProgress: Set<ElectronicAuctionProgress>?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionModalities @JsonCreator constructor(

        val url: String?,

        val eligibleMinimumDifference: Value?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionResult @JsonCreator constructor(

        val relatedBid: String?,

        val value: Value?
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionProgress @JsonCreator constructor(

        val id: String?,

        val period: Period?,

        val breakdown: Set<ElectronicAuctionProgressBreakdown>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionProgressBreakdown @JsonCreator constructor(

        val relatedBid: String?,

        val status: String?,

        val dateMet: LocalDateTime?,

        val value: Value?
)