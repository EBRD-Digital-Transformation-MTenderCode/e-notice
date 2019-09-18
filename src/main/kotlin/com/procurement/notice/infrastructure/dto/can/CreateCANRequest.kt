package com.procurement.notice.infrastructure.dto.can

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime
import java.util.*

data class CreateCANRequest(
    @field:JsonProperty("can") @param:JsonProperty("can") val can: CAN
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
}
