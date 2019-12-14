package com.procurement.notice.application.service.auction

import com.procurement.access.domain.model.enums.BusinessFunctionDocumentType
import com.procurement.notice.application.model.RequirementRsValue
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.bid.BidId
import com.procurement.notice.domain.model.document.DocumentId
import com.procurement.notice.domain.model.enums.AwardStatus
import com.procurement.notice.domain.model.enums.AwardStatusDetails
import com.procurement.notice.domain.model.enums.BidDocumentType
import com.procurement.notice.domain.model.enums.BidStatus
import com.procurement.notice.domain.model.enums.BidStatusDetails
import com.procurement.notice.domain.model.enums.BreakdownStatus
import com.procurement.notice.domain.model.enums.BusinessFunctionType
import com.procurement.notice.domain.model.enums.CriteriaRelatesTo
import com.procurement.notice.domain.model.enums.CriteriaSource
import com.procurement.notice.domain.model.enums.Scale
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.domain.model.enums.TypeOfSupplier
import com.procurement.notice.domain.model.lot.LotId
import com.procurement.notice.domain.model.money.Money
import com.procurement.notice.model.ocds.Requirement
import java.time.LocalDateTime

data class AuctionPeriodEndData(
    val tenderStatusDetails: TenderStatusDetails,
    val bids: List<Bid>,
    val criteria: Criteria?,
    val awards: List<Award>,
    val awardPeriod: AwardPeriod,
    val auctionPeriod: AuctionPeriod,
    val documents: List<Document>,
    val electronicAuctions: ElectronicAuctions
) {

    data class Bid(
        val id: BidId,
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
                val mainEconomicActivities: List<String>,
                val scale: Scale,
                val permits: List<Permit>,
                val bankAccounts: List<BankAccount>,
                val legalForm: LegalForm?
            ) {

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
            val id: DocumentId
        )

        data class RequirementResponse(
            val id: String,
            val title: String,
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

    data class Criteria(
        val id: String,
        val title: String,
        val source: CriteriaSource?,
        val description: String?,
        val relatedTo: CriteriaRelatesTo?,
        val relatedItem: String?,
        val requirementGroups: List<RequirementGroup>
    ) {

        data class RequirementGroup(
            val id: String,
            val description: String?,
            val requirements: List<Requirement>
        )
    }

    data class Award(
        val id: AwardId,
        val date: LocalDateTime,
        val status: AwardStatus,
        val statusDetails: AwardStatusDetails,
        val relatedLots: List<LotId>,
        val relatedBid: BidId,
        val value: Money,
        val suppliers: List<Supplier>,
        val weightedValue: Money?
    ) {

        data class Supplier(
            val id: String,
            val name: String
        )
    }

    data class AwardPeriod(
        val startDate: LocalDateTime
    )

    data class AuctionPeriod(
        val startDate: LocalDateTime,
        val endDate: LocalDateTime
    )

    data class Document(
        val documentType: BidDocumentType,
        val id: DocumentId,
        val title: String?,
        val description: String?,
        val relatedLots: List<LotId>,
        val datePublished: LocalDateTime,
        val url: String
    )

    data class ElectronicAuctions(
        val details: List<Detail>
    ) {
        data class Detail(
            val id: String,
            val relatedLot: LotId,
            val auctionPeriod: AuctionPeriod,
            val electronicAuctionModalities: List<ElectronicAuctionModality>,
            val electronicAuctionProgress: List<ElectronicAuctionProgres>,
            val electronicAuctionResult: List<ElectronicAuctionResult>
        ) {
            data class ElectronicAuctionModality(
                val url: String,
                val eligibleMinimumDifference: Money
            )

            data class ElectronicAuctionResult(
                val relatedBid: BidId,
                val value: Money
            )

            data class AuctionPeriod(
                val startDate: LocalDateTime,
                val endDate: LocalDateTime
            )

            data class ElectronicAuctionProgres(
                val id: String,
                val period: Period,
                val breakdowns: List<Breakdown>
            ) {
                data class Period(
                    val startDate: LocalDateTime,
                    val endDate: LocalDateTime
                )

                data class Breakdown(
                    val relatedBid: BidId,
                    val status: BreakdownStatus,
                    val dateMet: LocalDateTime,
                    val value: Money
                )
            }
        }
    }
}
