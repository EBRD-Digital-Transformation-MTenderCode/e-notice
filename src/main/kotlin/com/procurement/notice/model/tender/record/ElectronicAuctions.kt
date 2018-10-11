package com.procurement.notice.model.tender.record

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonInclude
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.Value


@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctions @JsonCreator constructor(

        val details: Set<ElectronicAuctionsDetails>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionsDetails @JsonCreator constructor(

        val id: String?,

        val relatedLot: String?,

        val auctionPeriod: Period?,

        val electronicAuctionModalities: Set<ElectronicAuctionModalities>
)

@JsonInclude(JsonInclude.Include.NON_NULL)
data class ElectronicAuctionModalities @JsonCreator constructor(

        val url: String?,

        val eligibleMinimumDifference: Value?
)