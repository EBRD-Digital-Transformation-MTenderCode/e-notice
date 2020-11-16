package com.procurement.notice.infrastructure.dto.award


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.bid.BidId
import com.procurement.notice.domain.model.enums.AwardStatus
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.BidDocumentType
import com.procurement.notice.domain.model.lot.LotId
import com.procurement.notice.domain.model.money.Money
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import com.procurement.notice.infrastructure.bind.money.MoneyDeserializer
import com.procurement.notice.infrastructure.bind.money.MoneySerializer
import java.math.BigDecimal
import java.time.LocalDateTime

data class EvaluateAwardRequest(
    @field:JsonProperty("award") @param:JsonProperty("award") val award: Award,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("nextAwardForUpdate") @param:JsonProperty("nextAwardForUpdate") val nextAwardForUpdate: NextAwardForUpdate?,

    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("bid") @param:JsonProperty("bid") val bid: Bid?
) {
    data class Award(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: AwardId,

        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @field:JsonProperty("status") @param:JsonProperty("status") val status: AwardStatus,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: AwardStatusDetails,
        @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<LotId>,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("relatedBid") @param:JsonProperty("relatedBid") val relatedBid: BidId?,

        @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,

        @field:JsonProperty("suppliers") @param:JsonProperty("suppliers") val suppliers: List<Supplier>,

        @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @param:JsonDeserialize(using = MoneyDeserializer::class)
        @field:JsonSerialize(using = MoneySerializer::class)
        @field:JsonProperty("weightedValue") @param:JsonProperty("weightedValue")val weightedValue: Money?
    ) {

        data class Value(
            @param:JsonDeserialize(using = AmountDeserializer::class)
            @field:JsonSerialize(using = AmountSerializer::class)
            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal?,

            @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
        )

        data class Supplier(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("name") @param:JsonProperty("name") val name: String
        )

        data class Document(
            @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

            @field:JsonProperty("url") @param:JsonProperty("url") val url: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<LotId>?
        )
    }

    data class NextAwardForUpdate(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: AwardId,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: AwardStatusDetails
    )

    data class Bid(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: BidId,

        @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?
    ) {
        data class Document(
            @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: BidDocumentType,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

            @field:JsonProperty("url") @param:JsonProperty("url") val url: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<LotId>?
        )
    }
}