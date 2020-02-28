package com.procurement.notice.infrastructure.dto.award

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.bid.BidId
import com.procurement.notice.domain.model.document.DocumentId
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.BidDocumentType
import com.procurement.notice.domain.model.lot.LotId
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime

data class AwardConsiderationRequest(
    @param:JsonProperty("award") @field:JsonProperty("award") val award: Award,
    @field:JsonProperty("bid") @param:JsonProperty("bid") val bid: Bid
) {
    data class Award(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: AwardId,
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: AwardStatusDetails
    )

    data class Bid(
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?,

        @field:JsonProperty("id") @param:JsonProperty("id") val id: BidId
    ) {
        data class Document(
            @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: BidDocumentType,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: DocumentId,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String?,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("url") @param:JsonProperty("url") val url: String?,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<LotId>?
        )
    }
}