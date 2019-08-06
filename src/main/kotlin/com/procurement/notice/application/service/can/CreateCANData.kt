package com.procurement.notice.application.service.can

import java.time.LocalDateTime
import java.util.*

data class CreateCANData(
    val can: CAN,
    val bids: List<Bid>,
    val lot: Lot
) {
    data class CAN(
        val id: UUID,
        val lotId: UUID,
        val awardId: UUID,
        val date: LocalDateTime,
        val status: String,
        val statusDetails: String
    )

    data class Bid(
        val id: UUID,
        val statusDetails: String
    )

    data class Lot(
        val id: UUID,
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
            val amount: Double,
            val currency: String
        )

        data class Option(val hasOptions: Boolean)

        data class Variant(val hasVariants: Boolean)

        data class Renewal(val hasRenewals: Boolean)

        data class RecurrentProcurement(val isRecurrent: Boolean)

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
                val postalCode: String,
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
                        val uri: String
                    )
                }
            }
        }
    }
}
