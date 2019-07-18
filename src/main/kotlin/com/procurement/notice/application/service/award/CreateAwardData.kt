package com.procurement.notice.application.service.award

import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateAwardData(
    val award: Award,
    val lots: List<Lot>?
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
            val amount: BigDecimal,
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

    data class Lot(
        val id: String,
        val title: String,
        val description: String,
        val status: String,
        val statusDetails: String,
        val value: Value,
        val options: List<Option>,
        val variants: List<Variant>,
        val renewals: List<Renewal>,
        val recurrentProcurement: List<RecurrentProcurement>,
        val contractPeriod: ContractPeriod,
        val placeOfPerformance: PlaceOfPerformance
    ) {

        data class Value(
            val amount: BigDecimal,
            val currency: String
        )

        data class Option(
            val hasOptions: Boolean
        )

        data class Variant(
            val hasVariants: Boolean
        )

        data class Renewal(
            val hasRenewals: Boolean
        )

        data class RecurrentProcurement(
            val isRecurrent: Boolean
        )

        data class ContractPeriod(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        )

        data class PlaceOfPerformance(
            val address: Address,
            val description: String
        ) {

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
        }
    }
}
