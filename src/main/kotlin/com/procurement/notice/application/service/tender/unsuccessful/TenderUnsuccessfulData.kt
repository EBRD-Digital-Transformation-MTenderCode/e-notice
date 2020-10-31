package com.procurement.notice.application.service.tender.unsuccessful

import com.procurement.access.domain.model.enums.BusinessFunctionDocumentType
import com.procurement.access.domain.model.enums.LotStatus
import com.procurement.notice.application.model.RequirementRsValue
import com.procurement.notice.domain.model.enums.AwardStatus
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.BidDocumentType
import com.procurement.notice.domain.model.enums.BidStatus
import com.procurement.notice.domain.model.enums.BidStatusDetails
import com.procurement.notice.domain.model.enums.BusinessFunctionType
import com.procurement.notice.domain.model.enums.Scale
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.domain.model.enums.TypeOfSupplier
import com.procurement.notice.domain.model.lot.LotId
import com.procurement.notice.domain.model.money.Money
import java.time.LocalDateTime

data class TenderUnsuccessfulData(
    val tender: Tender,
    val bids: List<Bid>,
    val awards: List<Award>,
    val documents: List<Document>,
    val unsuccessfulLots: List<UnsuccessfulLot>
) {

    data class Tender(
        val status: TenderStatus,
        val statusDetails: TenderStatusDetails
    )

    data class Bid(
        val id: String,
        val date: LocalDateTime,
        val status: BidStatus,
        val statusDetails: BidStatusDetails,
        val tenderers: List<Tenderer>,
        val value: Money,
        val documents: List<Document>,
        val requirementResponses: List<RequirementResponse>,
        val relatedLots: List<LotId>
    ) {

        data class Tenderer(
            val id: String,
            val name: String,
            val identifier: Identifier,
            val additionalIdentifiers: List<AdditionalIdentifier>,
            val address: Address,
            val contactPoint: ContactPoint,
            val persons: List<Person>,
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
                val details: Details
            ) {

                data class Details(
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

            data class Person(
                val title: String,
                val name: String,
                val identifier: Identifier,
                val businessFunctions: List<BusinessFunction>
            ) {

                data class Identifier(
                    val scheme: String,
                    val id: String,
                    val uri: String?
                )

                data class BusinessFunction(
                    val id: String,
                    val type: BusinessFunctionType,
                    val jobTitle: String,
                    val period: Period,
                    val documents: List<Document>
                ) {

                    data class Period(
                        val startDate: LocalDateTime
                    )

                    data class Document(
                        val id: String,
                        val documentType: BusinessFunctionDocumentType,
                        val title: String,
                        val description: String?,
                        val datePublished: LocalDateTime,
                        val url: String
                    )
                }
            }

            data class Details(
                val typeOfSupplier: TypeOfSupplier?,
                val mainEconomicActivities: List<MainEconomicActivity>,
                val scale: Scale,
                val permits: List<Permit>,
                val bankAccounts: List<BankAccount>,
                val legalForm: LegalForm?
            ) {
                data class MainEconomicActivity(
                    val scheme: String,
                    val id: String,
                    val description: String,
                    val uri: String?
                )

                data class Permit(
                    val scheme: String,
                    val id: String,
                    val url: String?,
                    val details: Details
                ) {

                    data class Details(
                        val issuedBy: IssuedBy,
                        val issuedThought: IssuedThought,
                        val validityPeriod: ValidityPeriod
                    ) {

                        data class IssuedBy(
                            val id: String,
                            val name: String
                        )

                        data class IssuedThought(
                            val id: String,
                            val name: String
                        )

                        data class ValidityPeriod(
                            val startDate: LocalDateTime,
                            val endDate: LocalDateTime?
                        )
                    }
                }

                data class BankAccount(
                    val description: String,
                    val bankName: String,
                    val address: Address,
                    val identifier: Identifier,
                    val accountIdentification: AccountIdentification,
                    val additionalAccountIdentifiers: List<AdditionalAccountIdentifier>
                ) {

                    data class Address(
                        val streetAddress: String,
                        val postalCode: String?,
                        val details: Details
                    ) {

                        data class Details(
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

                    data class Identifier(
                        val scheme: String,
                        val id: String
                    )

                    data class AccountIdentification(
                        val scheme: String,
                        val id: String
                    )

                    data class AdditionalAccountIdentifier(
                        val scheme: String,
                        val id: String
                    )
                }

                data class LegalForm(
                    val scheme: String,
                    val id: String,
                    val description: String,
                    val uri: String?
                )
            }
        }

        data class Document(
            val id: String
        )

        data class RequirementResponse(
            val id: String,
            val title: String?,
            val description: String?,
            val value: RequirementRsValue,
            val requirement: Requirement,
            val period: Period?
        ) {

            data class Requirement(
                val id: String
            )

            data class Period(
                val startDate: LocalDateTime,
                val endDate: LocalDateTime
            )
        }
    }

    data class Award(
        val id: String,
        val date: LocalDateTime,
        val status: AwardStatus,
        val statusDetails: AwardStatusDetails,
        val title: String,
        val description: String,
        val relatedLots: List<LotId>
    )

    data class Document(
        val documentType: BidDocumentType,
        val id: String,
        val title: String?,
        val description: String?,
        val relatedLots: List<LotId>,
        val datePublished: LocalDateTime,
        val url: String
    )

    data class UnsuccessfulLot(
        val id: LotId,
        val status: LotStatus
    )
}
