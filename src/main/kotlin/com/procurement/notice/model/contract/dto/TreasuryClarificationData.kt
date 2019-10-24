package com.procurement.notice.model.contract.dto

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class TreasuryClarificationData(
    val contract: Contract,
    val cans: List<Can>
) {
    data class Contract(
        val id: String,
        val date: LocalDateTime,
        val awardID: UUID,
        val status: String,
        val statusDetails: String,
        val title: String,
        val description: String,
        val period: Period,
        val documents: List<Document>,
        val milestones: List<Milestone>,
        val confirmationResponses: List<ConfirmationResponse>,
        val confirmationRequests: List<ConfirmationRequest>,
        val value: Value,
        val agreedMetrics: List<AgreedMetric>?
    ) {
        data class Period(
            val startDate: LocalDateTime,
            val endDate: LocalDateTime
        )

        data class Document(
            val documentType: String,
            val id: String,
            val datePublished: LocalDateTime,
            val url: String,
            val title: String?,
            val description: String?,
            val relatedLots: List<UUID>?,
            val relatedConfirmations: List<String>?
        )

        data class Milestone(
            val id: String,
            val relatedItems: List<UUID>?,
            val status: String,
            val additionalInformation: String?,
            val dueDate: LocalDateTime?,
            val title: String,
            val type: String,
            val description: String,
            val dateModified: LocalDateTime?,
            val dateMet: LocalDateTime?,
            val relatedParties: List<RelatedParty>
        ) {
            data class RelatedParty(
                val id: String,
                val name: String
            )
        }

        data class ConfirmationResponse(
            val id: String,
            val value: Value,
            val request: String
        ) {
            data class Value(
                val name: String,
                val id: String,
                val date: LocalDateTime,
                val relatedPerson: RelatedPerson,
                val verification: List<Verification>
            ) {
                data class RelatedPerson(
                    val id: String,
                    val name: String
                )

                data class Verification(
                    val type: String,
                    val value: String,
                    val rationale: String
                )
            }
        }

        data class ConfirmationRequest(
            val id: String,
            val type: String,
            val title: String,
            val description: String,
            val relatesTo: String,
            val relatedItem: UUID,
            val source: String,
            val requestGroups: List<RequestGroup>
        ) {
            data class RequestGroup(
                val id: String,
                val requests: List<Request>
            ) {
                data class Request(
                    val id: String,
                    val title: String,
                    val description: String,

                    val relatedPerson: RelatedPerson?
                ) {
                    data class RelatedPerson(
                        val id: String,
                        val name: String
                    )
                }
            }
        }

        data class Value(
            val amount: BigDecimal,
            val currency: String,
            val amountNet: BigDecimal,
            val valueAddedTaxincluded: Boolean
        )

        data class AgreedMetric(
            var id: String?,
            val title: String?,
            val description: String?,
            val observations: List<Observation>?
        ) {
            data class Observation(
                val id: String?,
                val notes: String?,
                val measure: Any?,
                val unit: ObservationUnit?
            ) {
                data class ObservationUnit(
                    val id: String?,
                    val name: String?,
                    val scheme: String?
                )
            }
        }
    }

    data class Can(
        val id: String,
        val status: String,
        val statusDetails: String
    )
}