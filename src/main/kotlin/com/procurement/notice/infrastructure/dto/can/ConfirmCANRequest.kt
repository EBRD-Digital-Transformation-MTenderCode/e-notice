package com.procurement.notice.infrastructure.dto.can

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*

data class ConfirmCANRequest(
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("cans") @param:JsonProperty("cans") val cans: List<CAN>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("lots") @param:JsonProperty("lots") val lots: List<Lot>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("awards") @param:JsonProperty("awards") val awards: List<Award>,

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: List<Bid>?
) {

    data class CAN(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Lot(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Award(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )

    data class Bid(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )
}
