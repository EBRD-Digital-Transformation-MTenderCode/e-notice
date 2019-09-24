package com.procurement.notice.infrastructure.dto.can

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime
import java.util.*

@JsonIgnoreProperties(ignoreUnknown = true)
data class CreateProtocolRequest(
    @field:JsonProperty("can") @param:JsonProperty("can") val can: CAN,

    @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: List<Bid>?,

    @field:JsonProperty("lot") @param:JsonProperty("lot") val lot: Lot
) {
    data class CAN(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("lotId") @param:JsonProperty("lotId") val lotId: UUID,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("awardId") @param:JsonProperty("awardId") val awardId: UUID?,

        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime,

        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Bid(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Lot(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )
}
