package com.procurement.notice.infrastructure.dto.can

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import java.time.LocalDateTime
import java.util.*

data class CreateProtocolRequest(
    @field:JsonProperty("can") @param:JsonProperty("can") val can: CAN,
    @field:JsonProperty("bids") @param:JsonProperty("bids") val bids: List<Bid>,
    @field:JsonProperty("lot") @param:JsonProperty("lot") val lot: Lot
) {
    data class CAN(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: UUID,
        @field:JsonProperty("lotId") @param:JsonProperty("lotId") val lotId: UUID,
        @field:JsonProperty("awardId") @param:JsonProperty("awardId") val awardId: UUID,

        @JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @JsonSerialize(using = JsonDateTimeSerializer::class)
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
        @field:JsonProperty("title") @param:JsonProperty("title") val title: String,
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,
        @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
        @field:JsonProperty("options") @param:JsonProperty("options") val options: List<Option>,
        @field:JsonProperty("variants") @param:JsonProperty("variants") val variants: List<Variant>,
        @field:JsonProperty("renewals") @param:JsonProperty("renewals") val renewals: List<Renewal>,
        @field:JsonProperty("recurrentProcurement") @param:JsonProperty("recurrentProcurement") val recurrentProcurement: List<RecurrentProcurement>,
        @field:JsonProperty("contractPeriod") @param:JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,
        @field:JsonProperty("placeOfPerformance") @param:JsonProperty("placeOfPerformance") val placeOfPerformance: PlaceOfPerformance
    ) {

        data class Value(
            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: Double,
            @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
        )

        data class Option(
            @field:JsonProperty("hasOptions") @param:JsonProperty("hasOptions") val hasOptions: Boolean
        )

        data class Variant(
            @field:JsonProperty("hasVariants") @param:JsonProperty("hasVariants") val hasVariants: Boolean
        )

        data class Renewal(
            @field:JsonProperty("hasRenewals") @param:JsonProperty("hasRenewals") val hasRenewals: Boolean
        )

        data class RecurrentProcurement(
            @get:JsonProperty("isRecurrent") @param:JsonProperty("isRecurrent") val isRecurrent: Boolean
        )

        data class ContractPeriod(
            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime,

            @JsonDeserialize(using = JsonDateTimeDeserializer::class)
            @JsonSerialize(using = JsonDateTimeSerializer::class)
            @field:JsonProperty("endDate") @param:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class PlaceOfPerformance(
            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("description") @param:JsonProperty("description") val description: String
        ) {
            data class Address(
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String,
                @field:JsonProperty("addressDetails") @param:JsonProperty("addressDetails") val addressDetails: AddressDetails
            ) {
                data class AddressDetails(
                    @field:JsonProperty("country") @param:JsonProperty("country") val country: Country,
                    @field:JsonProperty("region") @param:JsonProperty("region") val region: Region,
                    @field:JsonProperty("locality") @param:JsonProperty("locality") val locality: Locality
                ) {

                    data class Country(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    data class Region(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )

                    data class Locality(
                        @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                        @field:JsonProperty("description") @param:JsonProperty("description") val description: String,
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String
                    )
                }
            }
        }
    }
}
