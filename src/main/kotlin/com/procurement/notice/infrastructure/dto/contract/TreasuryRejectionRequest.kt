package com.procurement.notice.infrastructure.dto.contract


import com.fasterxml.jackson.annotation.JsonInclude
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.databind.annotation.JsonSerialize
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.infrastructure.bind.amount.AmountDeserializer
import com.procurement.notice.infrastructure.bind.amount.AmountSerializer
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

data class TreasuryRejectionRequest(
    @param:JsonProperty("contract") @field:JsonProperty("contract") val contract: Contract,
    @param:JsonProperty("cans") @field:JsonProperty("cans") val cans: List<Can>
) {
    data class Contract(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
        @param:JsonProperty("date") @field:JsonProperty("date") val date: LocalDateTime,
        @param:JsonProperty("awardId") @field:JsonProperty("awardId") val awardId: AwardId,
        @param:JsonProperty("status") @field:JsonProperty("status") val status: String,
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: String,
        @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
        @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
        @param:JsonProperty("period") @field:JsonProperty("period") val period: Period,
        @param:JsonProperty("documents") @field:JsonProperty("documents") val documents: List<Document>,
        @param:JsonProperty("milestones") @field:JsonProperty("milestones") val milestones: List<Milestone>,
        @param:JsonProperty("confirmationResponses") @field:JsonProperty("confirmationResponses") val confirmationResponses: List<ConfirmationResponse>,
        @param:JsonProperty("confirmationRequests") @field:JsonProperty("confirmationRequests") val confirmationRequests: List<ConfirmationRequest>,
        @param:JsonProperty("value") @field:JsonProperty("value") val value: Value
    ) {
        data class Period(
            @param:JsonProperty("startDate") @field:JsonProperty("startDate") val startDate: LocalDateTime,
            @param:JsonProperty("endDate") @field:JsonProperty("endDate") val endDate: LocalDateTime
        )

        data class Document(
            @param:JsonProperty("documentType") @field:JsonProperty("documentType") val documentType: String,
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("datePublished") @field:JsonProperty("datePublished") val datePublished: LocalDateTime,
            @param:JsonProperty("url") @field:JsonProperty("url") val url: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("title") @field:JsonProperty("title") val title: String?,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String?,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @param:JsonProperty("relatedLots") @field:JsonProperty("relatedLots") val relatedLots: List<UUID>?,

            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @param:JsonProperty("relatedConfirmations") @field:JsonProperty("relatedConfirmations") val relatedConfirmations: List<String>?
        )

        data class Milestone(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @JsonInclude(JsonInclude.Include.NON_EMPTY)
            @param:JsonProperty("relatedItems") @field:JsonProperty("relatedItems") val relatedItems: List<UUID>?,

            @param:JsonProperty("status") @field:JsonProperty("status") val status: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("additionalInformation") @field:JsonProperty("additionalInformation") val additionalInformation: String?,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("dueDate") @field:JsonProperty("dueDate") val dueDate: LocalDateTime?,

            @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
            @param:JsonProperty("type") @field:JsonProperty("type") val type: String,
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("dateModified") @field:JsonProperty("dateModified") val dateModified: LocalDateTime?,

            @JsonInclude(JsonInclude.Include.NON_NULL)
            @param:JsonProperty("dateMet") @field:JsonProperty("dateMet") val dateMet: LocalDateTime?,

            @param:JsonProperty("relatedParties") @field:JsonProperty("relatedParties") val relatedParties: List<RelatedParty>
        ) {
            data class RelatedParty(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("name") @field:JsonProperty("name") val name: String
            )
        }

        data class ConfirmationResponse(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("value") @field:JsonProperty("value") val value: Value,
            @param:JsonProperty("request") @field:JsonProperty("request") val request: String
        ) {
            data class Value(
                @param:JsonProperty("name") @field:JsonProperty("name") val name: String,
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("date") @field:JsonProperty("date") val date: LocalDateTime,

                @JsonInclude(JsonInclude.Include.NON_NULL)
                @param:JsonProperty("relatedPerson") @field:JsonProperty("relatedPerson") val relatedPerson: RelatedPerson?,

                @param:JsonProperty("verification") @field:JsonProperty("verification") val verification: List<Verification>
            ) {
                data class RelatedPerson(
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                    @param:JsonProperty("name") @field:JsonProperty("name") val name: String
                )

                data class Verification(
                    @param:JsonProperty("type") @field:JsonProperty("type") val type: String,
                    @param:JsonProperty("value") @field:JsonProperty("value") val value: String,
                    @param:JsonProperty("rationale") @field:JsonProperty("rationale") val rationale: String
                )
            }
        }

        data class ConfirmationRequest(
            @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
            @param:JsonProperty("type") @field:JsonProperty("type") val type: String,
            @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
            @param:JsonProperty("description") @field:JsonProperty("description") val description: String,
            @param:JsonProperty("relatesTo") @field:JsonProperty("relatesTo") val relatesTo: String,
            @param:JsonProperty("relatedItem") @field:JsonProperty("relatedItem") val relatedItem: String,
            @param:JsonProperty("source") @field:JsonProperty("source") val source: String,
            @param:JsonProperty("requestGroups") @field:JsonProperty("requestGroups") val requestGroups: List<RequestGroup>
        ) {
            data class RequestGroup(
                @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                @param:JsonProperty("requests") @field:JsonProperty("requests") val requests: List<Request>
            ) {
                data class Request(
                    @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                    @param:JsonProperty("title") @field:JsonProperty("title") val title: String,
                    @param:JsonProperty("description") @field:JsonProperty("description") val description: String,

                    @JsonInclude(JsonInclude.Include.NON_NULL)
                    @param:JsonProperty("relatedPerson") @field:JsonProperty("relatedPerson") val relatedPerson: RelatedPerson?
                ) {
                    data class RelatedPerson(
                        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
                        @param:JsonProperty("name") @field:JsonProperty("name") val name: String
                    )
                }
            }
        }

        data class Value(
            @JsonDeserialize(using = AmountDeserializer::class)
            @JsonSerialize(using = AmountSerializer::class)
            @param:JsonProperty("amount") @field:JsonProperty("amount") val amount: BigDecimal,

            @param:JsonProperty("currency") @field:JsonProperty("currency") val currency: String,

            @JsonDeserialize(using = AmountDeserializer::class)
            @JsonSerialize(using = AmountSerializer::class)
            @param:JsonProperty("amountNet") @field:JsonProperty("amountNet") val amountNet: BigDecimal,
            @param:JsonProperty("valueAddedTaxincluded") @field:JsonProperty("valueAddedTaxincluded") val valueAddedTaxincluded: Boolean
        )
    }

    data class Can(
        @param:JsonProperty("id") @field:JsonProperty("id") val id: String,
        @param:JsonProperty("status") @field:JsonProperty("status") val status: String,
        @param:JsonProperty("statusDetails") @field:JsonProperty("statusDetails") val statusDetails: String
    )
}
