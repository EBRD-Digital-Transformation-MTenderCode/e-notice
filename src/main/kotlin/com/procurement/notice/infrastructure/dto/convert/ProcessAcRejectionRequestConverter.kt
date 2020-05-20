package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.contract.rejection.TreasuryRejectionData
import com.procurement.notice.infrastructure.dto.contract.TreasuryRejectionRequest

fun TreasuryRejectionRequest.convert(): TreasuryRejectionData {
    return TreasuryRejectionData(
        contract = this.contract
            .let { contract ->
                TreasuryRejectionData.Contract(
                    id = contract.id,
                    documents = contract.documents
                        .map { document ->
                            TreasuryRejectionData.Contract.Document(
                                documentType = document.documentType,
                                relatedConfirmations = document.relatedConfirmations,
                                relatedLots = document.relatedLots,
                                description = document.description,
                                title = document.title,
                                id = document.id,
                                url = document.url,
                                datePublished = document.datePublished
                            )
                        },
                    title = contract.title,
                    description = contract.description,
                    period = contract.period
                        .let { period ->
                            TreasuryRejectionData.Contract.Period(
                                startDate = period.startDate,
                                endDate = period.endDate
                            )
                        },
                    value = contract.value
                        .let { value ->
                            TreasuryRejectionData.Contract.Value(
                                amount = value.amount,
                                currency = value.currency,
                                amountNet = value.amountNet,
                                valueAddedTaxincluded = value.valueAddedTaxincluded
                            )
                        },
                    awardId = contract.awardId,
                    confirmationRequests = contract.confirmationRequests
                        .map { confirmationRequest ->
                            TreasuryRejectionData.Contract.ConfirmationRequest(
                                title = confirmationRequest.title,
                                description = confirmationRequest.description,
                                id = confirmationRequest.id,
                                relatedItem = confirmationRequest.relatedItem,
                                relatesTo = confirmationRequest.relatesTo,
                                requestGroups = confirmationRequest.requestGroups
                                    .map { requestGroup ->
                                        TreasuryRejectionData.Contract.ConfirmationRequest.RequestGroup(
                                            id = requestGroup.id,
                                            requests = requestGroup.requests
                                                .map { request ->
                                                    TreasuryRejectionData.Contract.ConfirmationRequest.RequestGroup.Request(
                                                        id = request.id,
                                                        description = request.description,
                                                        title = request.title,
                                                        relatedPerson = request.relatedPerson
                                                            ?.let { relatedPerson ->
                                                                TreasuryRejectionData.Contract.ConfirmationRequest.RequestGroup.Request.RelatedPerson(
                                                                    id = relatedPerson.id,
                                                                    name = relatedPerson.name
                                                                )
                                                            }
                                                    )
                                                }
                                        )
                                    },
                                source = confirmationRequest.source,
                                type = confirmationRequest.type
                            )
                        },
                    confirmationResponses = contract.confirmationResponses
                        .map { confirmationResponse ->
                            TreasuryRejectionData.Contract.ConfirmationResponse(
                                id = confirmationResponse.id,
                                value = confirmationResponse.value
                                    .let { value ->
                                        TreasuryRejectionData.Contract.ConfirmationResponse.Value(
                                            name = value.name,
                                            id = value.id,
                                            relatedPerson = value.relatedPerson
                                                ?.let { relatedPerson ->
                                                    TreasuryRejectionData.Contract.ConfirmationResponse.Value.RelatedPerson(
                                                        id = relatedPerson.id,
                                                        name = relatedPerson.name
                                                    )
                                                },
                                            date = value.date,
                                            verification = value.verification
                                                .map { verification ->
                                                    TreasuryRejectionData.Contract.ConfirmationResponse.Value.Verification(
                                                        type = verification.type,
                                                        value = verification.value,
                                                        rationale = verification.rationale
                                                    )
                                                }
                                        )
                                    },
                                request = confirmationResponse.request
                            )
                        },
                    date = contract.date,
                    milestones = contract.milestones
                        .map { milestone ->
                            TreasuryRejectionData.Contract.Milestone(
                                id = milestone.id,
                                type = milestone.type,
                                title = milestone.title,
                                description = milestone.description,
                                additionalInformation = milestone.additionalInformation,
                                dateMet = milestone.dateMet,
                                dateModified = milestone.dateModified,
                                dueDate = milestone.dueDate,
                                relatedItems = milestone.relatedItems,
                                relatedParties = milestone.relatedParties
                                    .map { relatedParty ->
                                        TreasuryRejectionData.Contract.Milestone.RelatedParty(
                                            id = relatedParty.id,
                                            name = relatedParty.name
                                        )
                                    },
                                status = milestone.status
                            )
                        },
                    status = contract.status,
                    statusDetails = contract.statusDetails
                )
            },
        cans = this.cans
            .map { can ->
                TreasuryRejectionData.Can(
                    id = can.id,
                    statusDetails = can.statusDetails,
                    status = can.status
                )
            }
    )
}
