package com.procurement.notice.infrastructure.dto.award.auction


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.access.domain.model.enums.LotStatus
import com.procurement.notice.domain.model.enums.AwardStatus
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.domain.model.money.Money
import com.procurement.notice.infrastructure.bind.money.MoneyDeserializer
import com.procurement.notice.infrastructure.bind.money.MoneySerializer
import java.time.LocalDateTime
import java.util.*

data class StartAwardPeriodAuctionRequest(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @param:JsonProperty("awards") @field:JsonProperty("awards") val awards: List<Award>?,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @param:JsonProperty("unsuccessfulLots") @field:JsonProperty("unsuccessfulLots") val unsuccessfulLots: List<UnsuccessfulLot>?,

    @param:JsonProperty("tender") @field:JsonProperty("tender") val tender: Tender,
    @param:JsonProperty("electronicAuctions") @field:JsonProperty("electronicAuctions") val electronicAuctions: ElectronicAuctions
) {
    data class Award(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
        @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
        @param:JsonProperty("date") @field:JsonProperty("date") val date: LocalDateTime,
        @param:JsonProperty("status") @field:JsonProperty("status") val status: AwardStatus,
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: AwardStatusDetails,
        @param:JsonProperty("relatedLots") @field:JsonProperty("relatedLots") val relatedLots: List<UUID>
    )

    data class UnsuccessfulLot(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: UUID,
        @param:JsonProperty("status") @field:JsonProperty("status") val status: LotStatus
    )

    data class Tender(
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: TenderStatusDetails
    )

    data class ElectronicAuctions(
        @param:JsonProperty("details") @field:JsonProperty("details") val details: List<Detail>
    ) {
        data class Detail(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("relatedLot") @field:JsonProperty("relatedLot") val relatedLot: UUID,
            @param:JsonProperty("auctionPeriod") @field:JsonProperty("auctionPeriod") val auctionPeriod: AuctionPeriod,
            @param:JsonProperty("electronicAuctionModalities") @field:JsonProperty("electronicAuctionModalities") val electronicAuctionModalities: List<ElectronicAuctionModality>
        ) {
            data class AuctionPeriod(
                @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime
            )

            data class ElectronicAuctionModality(
                @param:JsonProperty("url") @field:JsonProperty("url") val url: String,

                @JsonDeserialize(using = MoneyDeserializer::class)
                @JsonSerialize(using = MoneySerializer::class)
                @param:JsonProperty("eligibleMinimumDifference") @field:JsonProperty("eligibleMinimumDifference") val eligibleMinimumDifference: Money
            )
        }
    }
}