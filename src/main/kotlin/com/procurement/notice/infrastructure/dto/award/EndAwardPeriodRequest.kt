package com.procurement.notice.infrastructure.dto.award

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime

data class EndAwardPeriodRequest(
    @field:JsonInclude(JsonInclude.Include.NON_NULL)
    @field:JsonProperty("contract") @param:JsonProperty("contract") val contract: Contract?,

    @field:JsonProperty("cans") @param:JsonProperty("cans") val cans: List<CAN>,
    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender,
    @field:JsonProperty("lots") @param:JsonProperty("lots") val lots: List<Lot>,
    @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<Award>,
    @field:JsonProperty("awardPeriod") @param:JsonProperty("awardPeriod") val awardPeriod: AwardPeriod,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: List<Bid>?
) {

    data class Contract(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,
        @field:JsonProperty("milestones") @param:JsonProperty("milestones") val milestones: List<Milestone>
    ) {

        data class Milestone(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("relatedItems") @param:JsonProperty("relatedItems") val relatedItems: List<String>?,

            @field:JsonProperty("status") @param:JsonProperty("status") val status: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("additionalInformation") @param:JsonProperty("additionalInformation") val additionalInformation: String?,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("dueDate") @param:JsonProperty("dueDate") val dueDate: LocalDateTime?,

            @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
            @field:JsonProperty("type") @param:JsonProperty("type") val type: String,
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("dateModified") @param:JsonProperty("dateModified") val dateModified: LocalDateTime?,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("dateMet") @param:JsonProperty("dateMet") val dateMet: LocalDateTime?,

            @field:JsonProperty("relatedParties") @param:JsonProperty("relatedParties") val relatedParties: List<RelatedParty>
        ) {

            data class RelatedParty(
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String
            )
        }
    }

    data class CAN(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Tender(
        @field:JsonProperty("status") @param:JsonProperty("status") val status: TenderStatus,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: TenderStatusDetails
    )

    data class Lot(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Award(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class AwardPeriod(
        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
    )

    data class Bid(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )
}
