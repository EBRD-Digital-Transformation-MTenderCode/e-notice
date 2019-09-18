package com.procurement.notice.infrastructure.dto.award

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeDeserializer
import com.procurement.notice.infrastructure.bind.date.JsonDateTimeSerializer
import com.procurement.notice.model.ocds.TenderStatusDetails
import java.math.BigDecimal
import java.time.LocalDateTime

data class StartAwardPeriodRequest(
    @field:JsonProperty("award") @param:JsonProperty("award") val award: Award,
    @field:JsonProperty("awardPeriod") @param:JsonProperty("awardPeriod") val awardPeriod: AwardPeriod,
    @field:JsonProperty("tenderStatusDetails") @param:JsonProperty("tenderStatusDetails") val tenderStatusDetails: TenderStatusDetails
) {

    data class Award(
        @field:JsonProperty("id") @param:JsonProperty("id") val id: String,

        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("date") @param:JsonProperty("date") val date: LocalDateTime,
        @field:JsonProperty("status") @param:JsonProperty("status") val status: String,
        @field:JsonProperty("statusDetails") @param:JsonProperty("statusDetails") val statusDetails: String,
        @field:JsonProperty("relatedLots") @param:JsonProperty("relatedLots") val relatedLots: List<String>,

        @field:JsonInclude(JsonInclude.Include.NON_NULL)
        @field:JsonProperty("description") @param:JsonProperty("description") val description: String?,

        @field:JsonProperty("value") @param:JsonProperty("value") val value: Value,
        @field:JsonProperty("suppliers") @param:JsonProperty("suppliers") val suppliers: List<Supplier>
    ) {

        data class Value(
            @param:JsonDeserialize(using = AmountDeserializer::class)
            @field:JsonSerialize(using = AmountSerializer::class)
            @field:JsonProperty("amount") @param:JsonProperty("amount") val amount: BigDecimal,

            @field:JsonProperty("currency") @param:JsonProperty("currency") val currency: String
        )

        data class Supplier(
            @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
            @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
            @field:JsonProperty("identifier") @param:JsonProperty("identifier") val identifier: Identifier,

            @field:JsonInclude(JsonInclude.Include.NON_EMPTY)
            @field:JsonProperty("additionalIdentifiers") @param:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>?,

            @field:JsonProperty("address") @param:JsonProperty("address") val address: Address,
            @field:JsonProperty("contactPoint") @param:JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @field:JsonProperty("details") @param:JsonProperty("details") val details: Details
        ) {

            data class Identifier(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
            )

            data class AdditionalIdentifier(
                @field:JsonProperty("scheme") @param:JsonProperty("scheme") val scheme: String,
                @field:JsonProperty("id") @param:JsonProperty("id") val id: String,
                @field:JsonProperty("legalName") @param:JsonProperty("legalName") val legalName: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
            )

            data class Address(
                @field:JsonProperty("streetAddress") @param:JsonProperty("streetAddress") val streetAddress: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("postalCode") @param:JsonProperty("postalCode") val postalCode: String?,
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

                        @field:JsonInclude(JsonInclude.Include.NON_NULL)
                        @field:JsonProperty("uri") @param:JsonProperty("uri") val uri: String?
                    )
                }
            }

            data class ContactPoint(
                @field:JsonProperty("name") @param:JsonProperty("name") val name: String,
                @field:JsonProperty("email") @param:JsonProperty("email") val email: String,
                @field:JsonProperty("telephone") @param:JsonProperty("telephone") val telephone: String,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("faxNumber") @param:JsonProperty("faxNumber") val faxNumber: String?,

                @field:JsonInclude(JsonInclude.Include.NON_NULL)
                @field:JsonProperty("url") @param:JsonProperty("url") val url: String?
            )

            data class Details(
                @field:JsonProperty("scale") @param:JsonProperty("scale") val scale: String
            )
        }
    }

    data class AwardPeriod(
        @param:JsonDeserialize(using = JsonDateTimeDeserializer::class)
        @field:JsonSerialize(using = JsonDateTimeSerializer::class)
        @field:JsonProperty("startDate") @param:JsonProperty("startDate") val startDate: LocalDateTime
    )
}
