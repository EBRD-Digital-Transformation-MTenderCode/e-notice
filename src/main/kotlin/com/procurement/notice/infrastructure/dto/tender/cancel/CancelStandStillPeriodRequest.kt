package com.procurement.notice.infrastructure.dto.tender.cancel

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime

data class CancelStandStillPeriodRequest(
    @field:JsonProperty("standstillPeriod") @param:JsonProperty("standstillPeriod") val standstillPeriod: StandstillPeriod,
    @field:JsonProperty("amendments") @param:JsonProperty("amendments") val amendments: List<Amendment>,
    @field:JsonProperty("tender") @param:JsonProperty("tender") val tender: Tender
) {
    data class StandstillPeriod(
        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
    )

    data class Amendment(
        @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
        @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?
    ) {

        data class Document(
            @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,

            @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @field:JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

            @field:JsonProperty("url") @param:JsonProperty("url") val url: String,
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
        )
    }

    data class Tender(
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )
}
