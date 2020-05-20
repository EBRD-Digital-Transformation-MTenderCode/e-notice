package com.procurement.notice.infrastructure.dto.can

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime

data class CancelCANRequest(
    @field:JsonProperty("cancelledCan") @param:JsonProperty("cancelledCan") val cancelledCan: CancelledCAN,
    @field:JsonProperty("lot") @param:JsonProperty("lot") val lot: Lot
) {
    data class CancelledCAN(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,
        @field:JsonProperty("amendment") @param:JsonProperty("amendment") val amendment: Amendment
    ) {
        data class Amendment(
            @field:JsonProperty("rationale") @param:JsonProperty("rationale") val rationale: String,

            @field:JsonInclude(JsonInclude.Include.NON_NULL)
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("documents") @param:JsonProperty("documents") val documents: List<Document>?
        ) {
            data class Document(
                @field:JsonProperty("documentType") @param:JsonProperty("documentType") val documentType: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("url") @param:JsonProperty("url") val url: String,

                @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
                @field:JsonSerialize(using = JsonDateTimeSerializer::class)
                @field:JsonProperty("datePublished") @param:JsonProperty("datePublished") val datePublished: LocalDateTime,

                @field:JsonProperty("title") @param:JsonProperty("title") val title: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("description") @param:JsonProperty("description") val description: String?
            )
        }
    }

    data class Lot(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String
    )
}
