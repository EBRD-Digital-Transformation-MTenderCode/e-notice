package com.procurement.notice.application.service.fe.create

import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.domain.model.enums.BusinessFunctionType
import com.procurement.notice.domain.model.enums.CriteriaRelatesTo
import com.procurement.notice.domain.model.enums.CriteriaSource
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.model.ocds.QualificationSystemMethod
import com.procurement.notice.model.ocds.ReductionCriteria
import com.procurement.notice.model.ocds.Requirement
import org.w3c.dom.DocumentType
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateFeData(
    val preQualification: PreQualification,
    val tender: Tender
) {
    data class PreQualification(
        val period: Period
    ) {
        data class Period(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        )
    }

    data class Tender(
        val id: String,
        val status: TenderStatus,
        val statusDetails: TenderStatusDetails,
        val title: String,
        val description: String,
        val classification: Classification,
        val otherCriteria: OtherCriteria,
        val secondStage: SecondStage?,
        val enquiryPeriod: EnquiryPeriod,
        val acceleratedProcedure: AcceleratedProcedure,
        val designContest: DesignContest,
        val electronicWorkflows: ElectronicWorkflows,
        val jointProcurement: JointProcurement,
        val procedureOutsourcing: ProcedureOutsourcing,
        val framework: Framework,
        val dynamicPurchasingSystem: DynamicPurchasingSystem,
        val requiresElectronicCatalogue: Boolean,
        val legalBasis: String,
        val procurementMethod: ProcurementMethod,
        val procurementMethodDetails: String,
        val procurementMethodRationale: String?,
        val mainProcurementCategory: String?,
        val eligibilityCriteria: String,
        val contractPeriod: ContractPeriod,
        val procurementMethodModalities: List<String>,
        val criteria: List<Criteria>,
        val procuringEntity: ProcuringEntity,
        val value: Value,
        val submissionMethod: List<String>,
        val submissionMethodRationale: List<String>,
        val submissionMethodDetails: String,
        val documents: List<Document>
    ) {
        data class Classification(
            val scheme: String,
            val description: String,
            val id: String
        )

        data class OtherCriteria(
            val qualificationSystemMethods: List<QualificationSystemMethod>,
            val reductionCriteria: ReductionCriteria
        )

        data class SecondStage(
            val minimumCandidates: Long?,
            val maximumCandidates: Long?
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

        data class Criteria(
            val id: String,
            val title: String,
            val source: CriteriaSource,
            val relatesTo: CriteriaRelatesTo,
            val description: String?,
            val requirementGroups: List<RequirementGroup>
        ) {
            data class RequirementGroup(
                val id: String,
                val description: String?,
                val requirements: List<Requirement>
            )
        }

        data class ProcuringEntity(
            val id: String,
            val name: String,
            val identifier: Identifier,
            val additionalIdentifiers: List<AdditionalIdentifier>,
            val address: Address,
            val contactPoint: ContactPoint,
            val persones: List<Persone>
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

            data class Persone(
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
                        val url: String,
                        val datePublished: LocalDateTime,
                        val documentType: DocumentType,
                        val title: String,
                        val description: String?
                    )
                }
            }
        }

        data class Value(
            val amount: BigDecimal,
            val currency: String
        )

        data class Document(
            val documentType: DocumentType,
            val id: String,
            val url: String,
            val datePublished: LocalDateTime,
            val title: String,
            val description: String?
        )
    }
}