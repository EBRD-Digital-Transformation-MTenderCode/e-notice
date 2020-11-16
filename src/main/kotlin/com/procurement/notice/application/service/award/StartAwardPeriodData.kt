package com.procurement.notice.application.service.award

import com.procurement.notice.model.ocds.TenderStatusDetails
import java.math.BigDecimal
import java.time.LocalDateTime

data class StartAwardPeriodData(
    val award: Award,
    val awardPeriod: AwardPeriod,
    val tender: Tender
) {

    data class Award(
        val id: String,
        val date: LocalDateTime,
        val status: String,
        val statusDetails: String,
        val relatedLots: List<String>,
        val description: String?,
        val value: Value,
        val suppliers: List<Supplier>
    ) {

        data class Value(
            val amount: BigDecimal?,
            val currency: String
        )

        data class Supplier(
            val id: String,
            val name: String,
            val identifier: Identifier,
            val additionalIdentifiers: List<AdditionalIdentifier>?,

            val address: Address,
            val contactPoint: ContactPoint,
            val details: Details
        ) {

            data class Identifier(
                val scheme: String,
                val id: String,
                val legalName: String,
                val uri: String?
            )

            data class AdditionalIdentifier(
                val scheme: String,
                val id: String,
                val legalName: String,
                val uri: String?
            )

            data class Address(
                val streetAddress: String,
                val postalCode: String?,
                val addressDetails: AddressDetails
            ) {

                data class AddressDetails(
                    val country: Country,
                    val region: Region,
                    val locality: Locality
                ) {

                    data class Country(
                        val scheme: String,
                        val id: String,
                        val description: String,
                        val uri: String
                    )

                    data class Region(
                        val scheme: String,
                        val id: String,
                        val description: String,
                        val uri: String
                    )

                    data class Locality(
                        val scheme: String,
                        val id: String,
                        val description: String,
                        val uri: String?
                    )
                }
            }

            data class ContactPoint(
                val name: String,
                val email: String,
                val telephone: String,
                val faxNumber: String?,
                val url: String?
            )

            data class Details(
                val scale: String
            )
        }
    }

    data class AwardPeriod(
        val startDate: LocalDateTime
    )

    data class Tender(
        val statusDetails: TenderStatusDetails
    )
}