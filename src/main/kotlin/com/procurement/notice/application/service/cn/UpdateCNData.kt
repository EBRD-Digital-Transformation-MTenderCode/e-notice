package com.procurement.notice.application.service.cn

import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.domain.model.money.Money
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import java.math.BigDecimal
import java.time.LocalDateTime

data class UpdateCNData(
    val planning: Planning,
    val tender: Tender,
    val amendment: Amendment?,
    val isAuctionPeriodChanged: Boolean
) {

    data class Planning(
        val rationale: String?,
        val budget: Budget
    ) {

        data class Budget(
            val description: String?,
            val amount: Money,
            val isEuropeanUnionFunded: Boolean,
            val budgetBreakdowns: List<BudgetBreakdown>
        ) {

            data class BudgetBreakdown(
                val id: String,
                val description: String?,
                val amount: Money,
                val period: Period,
                val sourceParty: SourceParty,
                val europeanUnionFunding: EuropeanUnionFunding?
            ) {
                data class Period(
                    val startDate: LocalDateTime,
                    val endDate: LocalDateTime
                )

                data class SourceParty(
                    val id: String,
                    val name: String
                )

                data class EuropeanUnionFunding(
                    val projectIdentifier: String,
                    val projectName: String,
                    val uri: String?
                )
            }
        }
    }

    data class Tender(
        val id: String,
        val status: TenderStatus,
        val statusDetails: TenderStatusDetails,
        val title: String,
        val description: String,
        val classification: Classification,
        val tenderPeriod: TenderPeriod,
        val enquiryPeriod: EnquiryPeriod,
        val acceleratedProcedure: AcceleratedProcedure,
        val designContest: DesignContest,
        val electronicWorkflows: ElectronicWorkflows,
        val jointProcurement: JointProcurement,
        val procedureOutsourcing: ProcedureOutsourcing,
        val framework: Framework,
        val dynamicPurchasingSystem: DynamicPurchasingSystem,
        val legalBasis: String,
        val procurementMethod: ProcurementMethod,
        val procurementMethodDetails: String,
        val procurementMethodRationale: String?,
        val procurementMethodAdditionalInfo: String?,
        val mainProcurementCategory: String,
        val eligibilityCriteria: String,
        val contractPeriod: ContractPeriod,
        val procurementMethodModalities: List<String>,
        val auctionPeriod: AuctionPeriod?,
        val electronicAuctions: ElectronicAuctions?,
        val procuringEntity: ProcuringEntity,
        val value: Money,
        val lotGroups: List<LotGroup>,
        val lots: List<Lot>,
        val items: List<Item>,
        val requiresElectronicCatalogue: Boolean,
        val submissionMethod: List<String>,
        val submissionMethodRationale: List<String>,
        val submissionMethodDetails: String,
        val documents: List<Document>
    ) {

        data class Classification(
            val scheme: String,
            val id: String,
            val description: String
        )

        data class TenderPeriod(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        )

        data class EnquiryPeriod(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        )

        data class AcceleratedProcedure(
            val isAcceleratedProcedure: Boolean
        )

        data class DesignContest(
            val serviceContractAward: Boolean
        )

        data class ElectronicWorkflows(
            val useOrdering: Boolean,
            val usePayment: Boolean,
            val acceptInvoicing: Boolean
        )

        data class JointProcurement(
            val isJointProcurement: Boolean
        )

        data class ProcedureOutsourcing(
            val procedureOutsourced: Boolean
        )

        data class Framework(
            val isAFramework: Boolean
        )

        data class DynamicPurchasingSystem(
            val hasDynamicPurchasingSystem: Boolean
        )

        data class ContractPeriod(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        )

        data class AuctionPeriod(
            val startDate: LocalDateTime
        )

        data class ElectronicAuctions(
            val details: List<Detail>
        ) {

            data class Detail(
                val id: String,
                val relatedLot: String,
                val auctionPeriod: AuctionPeriod?,
                val electronicAuctionModalities: List<ElectronicAuctionModality>
            ) {

                data class AuctionPeriod(
                    val startDate: LocalDateTime
                )

                data class ElectronicAuctionModality(
                    val eligibleMinimumDifference: Money,
                    val url: String?
                )
            }
        }

        data class ProcuringEntity(
            val id: String,
            val name: String,
            val identifier: Identifier,
            val additionalIdentifiers: List<AdditionalIdentifier>,
            val address: Address,
            val persons: List<Person>,
            val contactPoint: ContactPoint
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

            data class Person(
                val title: String,
                val name: String,
                val identifier: Identifier,
                val businessFunctions: List<BusinessFunction>
            ) {

                data class Identifier(
                    val id: String,
                    val scheme: String,
                    val uri: String?
                )

                data class BusinessFunction(
                    val id: String,
                    val type: String,
                    val jobTitle: String,
                    val period: Period,
                    val documents: List<Document>
                ) {

                    data class Period(
                        val startDate: LocalDateTime
                    )

                    data class Document(
                        val documentType: String,
                        val id: String,
                        val title: String,
                        val url: String,
                        val datePublished: LocalDateTime,
                        val description: String?
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
        }

        data class LotGroup(
            val optionToCombine: Boolean
        )

        data class Lot(
            val id: String,
            val internalId: String?,
            val title: String,
            val description: String,
            val status: String,
            val statusDetails: String,
            val value: Money,
            val options: List<Option>,
            val variants: List<Variant>,
            val renewals: List<Renewal>,
            val recurrentProcurements: List<RecurrentProcurement>,
            val contractPeriod: ContractPeriod,
            val placeOfPerformance: PlaceOfPerformance?
        ) {

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
                val description: String?
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

        data class Item(
            val id: String,
            val internalId: String?,
            val classification: Classification,
            val additionalClassifications: List<AdditionalClassification>,
            val quantity: BigDecimal,
            val unit: Unit,
            val description: String,
            val relatedLot: String
        ) {

            data class Classification(
                val scheme: String,
                val id: String,
                val description: String
            )

            data class AdditionalClassification(
                val scheme: String,
                val id: String,
                val description: String
            )

            data class Unit(
                val id: String,
                val name: String
            )
        }

        data class Document(
            val documentType: String,
            val id: String,
            val title: String?,
            val description: String?,
            val url: String,
            val datePublished: LocalDateTime,
            val relatedLots: List<String>
        )
    }

    data class Amendment(
        val relatedLots: List<String>
    )
}
