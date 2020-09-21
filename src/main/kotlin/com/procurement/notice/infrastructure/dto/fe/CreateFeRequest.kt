package com.procurement.notice.infrastructure.dto.fe

import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.domain.model.enums.BusinessFunctionType
import com.procurement.notice.domain.model.enums.CriteriaRelatesTo
import com.procurement.notice.domain.model.enums.CriteriaSource
import com.procurement.notice.domain.model.enums.TenderStatus
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import com.procurement.notice.infrastructure.bind.criteria.requirement.RequirementDeserializer
import com.procurement.notice.infrastructure.bind.criteria.requirement.RequirementSerializer
import com.procurement.notice.model.ocds.QualificationSystemMethod
import com.procurement.notice.model.ocds.ReductionCriteria
import com.procurement.notice.model.ocds.Requirement
import org.w3c.dom.DocumentType
import java.math.BigDecimal
import java.time.LocalDateTime

data class CreateFeRequest(
    @param:JsonProperty("preQualification") @field:JsonProperty("preQualification") val preQualification: PreQualification,
    @param:JsonProperty("tender") @field:JsonProperty("tender") val tender: Tender
) {
    data class PreQualification(
        @param:JsonProperty("period") @field:JsonProperty("period") val period: Period
    ) {
        data class Period(
            @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime,
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )
    }

    data class Tender(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
        @param:JsonProperty("status") @field:JsonProperty("status") val status: TenderStatus,
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: TenderStatusDetails,
        @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
        @param:JsonProperty("classification") @field:JsonProperty("classification") val classification: Classification,
        @param:JsonProperty("otherCriteria") @field:JsonProperty("otherCriteria") val otherCriteria: OtherCriteria,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @param:JsonProperty("secondStage") @field:JsonProperty("secondStage") val secondStage: SecondStage?,

        @param:JsonProperty("enquiryPeriod") @field:JsonProperty("enquiryPeriod") val enquiryPeriod: EnquiryPeriod,
        @param:JsonProperty("acceleratedProcedure") @field:JsonProperty("acceleratedProcedure") val acceleratedProcedure: AcceleratedProcedure,
        @param:JsonProperty("designContest") @field:JsonProperty("designContest") val designContest: DesignContest,
        @param:JsonProperty("electronicWorkflows") @field:JsonProperty("electronicWorkflows") val electronicWorkflows: ElectronicWorkflows,
        @param:JsonProperty("jointProcurement") @field:JsonProperty("jointProcurement") val jointProcurement: JointProcurement,
        @param:JsonProperty("procedureOutsourcing") @field:JsonProperty("procedureOutsourcing") val procedureOutsourcing: ProcedureOutsourcing,
        @param:JsonProperty("framework") @field:JsonProperty("framework") val framework: Framework,
        @param:JsonProperty("dynamicPurchasingSystem") @field:JsonProperty("dynamicPurchasingSystem") val dynamicPurchasingSystem: DynamicPurchasingSystem,
        @param:JsonProperty("requiresElectronicCatalogue") @field:JsonProperty("requiresElectronicCatalogue") val requiresElectronicCatalogue: Boolean,
        @param:JsonProperty("legalBasis") @field:JsonProperty("legalBasis") val legalBasis: String,
        @param:JsonProperty("procurementMethod") @field:JsonProperty("procurementMethod") val procurementMethod: ProcurementMethod,
        @param:JsonProperty("procurementMethodDetails") @field:JsonProperty("procurementMethodDetails") val procurementMethodDetails: String,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @param:JsonProperty("procurementMethodRationale") @field:JsonProperty("procurementMethodRationale") val procurementMethodRationale: String?,

        @JsonInclude(JsonInclude.Include.NON_NULL)
        @param:JsonProperty("mainProcurementCategory") @field:JsonProperty("mainProcurementCategory") val mainProcurementCategory: String?,

        @param:JsonProperty("eligibilityCriteria") @field:JsonProperty("eligibilityCriteria") val eligibilityCriteria: String,
        @param:JsonProperty("contractPeriod") @field:JsonProperty("contractPeriod") val contractPeriod: ContractPeriod,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("procurementMethodModalities") @field:JsonProperty("procurementMethodModalities") val procurementMethodModalities: List<String>?,

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        @param:JsonProperty("criteria") @field:JsonProperty("criteria") val criteria: List<Criteria>?,
        @param:JsonProperty("procuringEntity") @field:JsonProperty("procuringEntity") val procuringEntity: ProcuringEntity,
        @param:JsonProperty("value") @field:JsonProperty("value") val value: Value,
        @param:JsonProperty("submissionMethod") @field:JsonProperty("submissionMethod") val submissionMethod: List<String>,
        @param:JsonProperty("submissionMethodRationale") @field:JsonProperty("submissionMethodRationale") val submissionMethodRationale: List<String>,
        @param:JsonProperty("submissionMethodDetails") @field:JsonProperty("submissionMethodDetails") val submissionMethodDetails: String,
        @param:JsonProperty("documents") @field:JsonProperty("documents") val documents: List<Document>
    ) {
        data class Classification(
            @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String
        )

        data class OtherCriteria(
            @param:JsonProperty("qualificationSystemMethods") @field:JsonProperty("qualificationSystemMethods") val qualificationSystemMethods: List<QualificationSystemMethod>,
            @param:JsonProperty("reductionCriteria") @field:JsonProperty("reductionCriteria") val reductionCriteria: ReductionCriteria
        )

        data class SecondStage(
            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("minimumCandidates") @field:JsonProperty("minimumCandidates") val minimumCandidates: Long?,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("maximumCandidates") @field:JsonProperty("maximumCandidates") val maximumCandidates: Long?
        )

        data class EnquiryPeriod(
            @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime,
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class AcceleratedProcedure(
            @param:JsonProperty("isAcceleratedProcedure") @field:JsonProperty("isAcceleratedProcedure") val isAcceleratedProcedure: Boolean
        )

        data class DesignContest(
            @param:JsonProperty("serviceContractAward") @field:JsonProperty("serviceContractAward") val serviceContractAward: Boolean
        )

        data class ElectronicWorkflows(
            @param:JsonProperty("useOrdering") @field:JsonProperty("useOrdering") val useOrdering: Boolean,
            @param:JsonProperty("usePayment") @field:JsonProperty("usePayment") val usePayment: Boolean,
            @param:JsonProperty("acceptInvoicing") @field:JsonProperty("acceptInvoicing") val acceptInvoicing: Boolean
        )

        data class JointProcurement(
            @param:JsonProperty("isJointProcurement") @field:JsonProperty("isJointProcurement") val isJointProcurement: Boolean
        )

        data class ProcedureOutsourcing(
            @param:JsonProperty("procedureOutsourced") @field:JsonProperty("procedureOutsourced") val procedureOutsourced: Boolean
        )

        data class Framework(
            @param:JsonProperty("isAFramework") @field:JsonProperty("isAFramework") val isAFramework: Boolean
        )

        data class DynamicPurchasingSystem(
            @param:JsonProperty("hasDynamicPurchasingSystem") @field:JsonProperty("hasDynamicPurchasingSystem") val hasDynamicPurchasingSystem: Boolean
        )

        data class ContractPeriod(
            @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime,
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class Criteria(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
            @param:JsonProperty("source") @field:JsonProperty("source") val source: CriteriaSource,
            @param:JsonProperty("relatesTo") @field:JsonProperty("relatesTo") val relatesTo: CriteriaRelatesTo,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String?,

            @param:JsonProperty("requirementGroups") @field:JsonProperty("requirementGroups") val requirementGroups: List<RequirementGroup>
        ) {
            data class RequirementGroup(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("description") @field:JsonProperty("description") val description: String?,

                @JsonDeserialize(using = RequirementDeserializer::class)
                @JsonSerialize(using = RequirementSerializer::class)
                @param:JsonProperty("requirements") @field:JsonProperty("requirements") val requirements: List<Requirement>
            )
        }

        data class ProcuringEntity(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("name") @field:JsonProperty("name") val name: String,
            @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: Identifier,
            @param:JsonProperty("additionalIdentifiers") @field:JsonProperty("additionalIdentifiers") val additionalIdentifiers: List<AdditionalIdentifier>,
            @param:JsonProperty("address") @field:JsonProperty("address") val address: Address,
            @param:JsonProperty("contactPoint") @field:JsonProperty("contactPoint") val contactPoint: ContactPoint,
            @param:JsonProperty("persones") @field:JsonProperty("persones") val persones: List<Persone>
        ) {
            data class Identifier(
                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("legalName") @field:JsonProperty("legalName") val legalName: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String?
            )

            data class AdditionalIdentifier(
                @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("legalName") @field:JsonProperty("legalName") val legalName: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String?
            )

            data class Address(
                @param:JsonProperty("streetAddress") @field:JsonProperty("streetAddress") val streetAddress: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("postalCode") @field:JsonProperty("postalCode") val postalCode: String?,

                @param:JsonProperty("addressDetails") @field:JsonProperty("addressDetails") val addressDetails: AddressDetails
            ) {
                data class AddressDetails(
                    @param:JsonProperty("country") @field:JsonProperty("country") val country: Country,
                    @param:JsonProperty("region") @field:JsonProperty("region") val region: Region,
                    @param:JsonProperty("locality") @field:JsonProperty("locality") val locality: Locality
                ) {
                    data class Country(
                        @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                        @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                    )

                    data class Region(
                        @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
                        @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String
                    )

                    data class Locality(
                        @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,

                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String?
                    )
                }
            }

            data class ContactPoint(
                @param:JsonProperty("name") @field:JsonProperty("name") val name: String,
                @param:JsonProperty("email") @field:JsonProperty("email") val email: String,
                @param:JsonProperty("telephone") @field:JsonProperty("telephone") val telephone: String,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("faxNumber") @field:JsonProperty("faxNumber") val faxNumber: String?,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("url") @field:JsonProperty("url") val url: String?
            )

            data class Persone(
                @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
                @param:JsonProperty("name") @field:JsonProperty("name") val name: String,
                @param:JsonProperty("identifier") @field:JsonProperty("identifier") val identifier: Identifier,
                @param:JsonProperty("businessFunctions") @field:JsonProperty("businessFunctions") val businessFunctions: List<BusinessFunction>
            ) {
                data class Identifier(
                    @param:JsonProperty("scheme") @field:JsonProperty("scheme") val scheme: String,
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @param:JsonProperty("uri") @field:JsonProperty("uri") val uri: String?
                )

                data class BusinessFunction(
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                    @param:JsonProperty("type") @field:JsonProperty("type") val type: BusinessFunctionType,
                    @param:JsonProperty("jobTitle") @field:JsonProperty("jobTitle") val jobTitle: String,
                    @param:JsonProperty("period") @field:JsonProperty("period") val period: Period,
                    @param:JsonProperty("documents") @field:JsonProperty("documents") val documents: List<Document>
                ) {
                    data class Period(
                        @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime
                    )

                    data class Document(
                        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                        @param:JsonProperty("url") @field:JsonProperty("url") val url: String,
                        @param:JsonProperty("datePublished") @field:JsonProperty("datePublished") val datePublished: LocalDateTime,
                        @param:JsonProperty("documentType") @field:JsonProperty("documentType") val documentType: DocumentType,
                        @param:JsonProperty("title") @field:JsonProperty("title") val title: String,

                        @JsonInclude(JsonInclude.Include.NON_NULL)
                        @param:JsonProperty("description") @field:JsonProperty("description") val description: String?
                    )
                }
            }
        }

        data class Value(
            @JsonDeserialize(using = AmountDeserializer::class)
            @JsonSerialize(using = AmountSerializer::class)
            @param:JsonProperty("amount") @field:JsonProperty("amount") val amount: BigDecimal,
            @param:JsonProperty("currency") @field:JsonProperty("currency") val currency: String
        )

        data class Document(
            @param:JsonProperty("documentType") @field:JsonProperty("documentType") val documentType: DocumentType,
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("url") @field:JsonProperty("url") val url: String,
            @param:JsonProperty("datePublished") @field:JsonProperty("datePublished") val datePublished: LocalDateTime,
            @param:JsonProperty("title") @field:JsonProperty("title") val title: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String?
        )
    }
}