package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.tender.periodEnd.TenderPeriodEndData
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.tender.periodEnd.TenderPeriodEndRequest
import com.procurement.notice.lib.errorIfEmpty
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.lib.orThrow

fun TenderPeriodEndRequest.convert(): TenderPeriodEndData =
    TenderPeriodEndData(
        tenderStatusDetails = this.tenderStatusDetails,
        bids = this.bids
            .mapIfNotEmpty { bid ->
                TenderPeriodEndData.Bid(
                    id = bid.id,
                    date = bid.date,
                    status = bid.status,
                    statusDetails = bid.statusDetails,
                    tenderers = bid.tenderers
                        .mapIfNotEmpty { tenderer ->
                            TenderPeriodEndData.Bid.Tenderer(
                                id = tenderer.id,
                                name = tenderer.name,
                                identifier = tenderer.identifier
                                    .let { identifier ->
                                        TenderPeriodEndData.Bid.Tenderer.Identifier(
                                            scheme = identifier.scheme,
                                            id = identifier.id,
                                            legalName = identifier.legalName,
                                            uri = identifier.uri
                                        )
                                    },
                                additionalIdentifiers = tenderer.additionalIdentifiers
                                    .errorIfEmpty {
                                        ErrorException(
                                            error = ErrorType.IS_EMPTY,
                                            message = "The bid '${bid.id}' contain empty list of the additional identifiers in tenderer '${tenderer.id}'."
                                        )
                                    }
                                    ?.map { additionalIdentifier ->
                                        TenderPeriodEndData.Bid.Tenderer.AdditionalIdentifier(
                                            scheme = additionalIdentifier.scheme,
                                            id = additionalIdentifier.id,
                                            legalName = additionalIdentifier.legalName,
                                            uri = additionalIdentifier.uri
                                        )
                                    }
                                    .orEmpty(),
                                address = tenderer.address
                                    .let { address ->
                                        TenderPeriodEndData.Bid.Tenderer.Address(
                                            streetAddress = address.streetAddress,
                                            postalCode = address.postalCode,
                                            details = address.details.let { details ->
                                                TenderPeriodEndData.Bid.Tenderer.Address.Details(
                                                    country = details.country.let { country ->
                                                        TenderPeriodEndData.Bid.Tenderer.Address.Details.Country(
                                                            scheme = country.scheme,
                                                            id = country.id,
                                                            description = country.description,
                                                            uri = country.uri
                                                        )
                                                    },
                                                    region = details.region.let { region ->
                                                        TenderPeriodEndData.Bid.Tenderer.Address.Details.Region(
                                                            scheme = region.scheme,
                                                            id = region.id,
                                                            description = region.description,
                                                            uri = region.uri
                                                        )
                                                    },
                                                    locality = details.locality.let { locality ->
                                                        TenderPeriodEndData.Bid.Tenderer.Address.Details.Locality(
                                                            scheme = locality.scheme,
                                                            id = locality.id,
                                                            description = locality.description,
                                                            uri = locality.uri
                                                        )
                                                    }
                                                )

                                            }
                                        )
                                    },
                                contactPoint = tenderer.contactPoint
                                    .let { contactPoint ->
                                        TenderPeriodEndData.Bid.Tenderer.ContactPoint(
                                            name = contactPoint.name,
                                            email = contactPoint.email,
                                            telephone = contactPoint.telephone,
                                            faxNumber = contactPoint.faxNumber,
                                            url = contactPoint.url
                                        )
                                    },
                                details = tenderer.details.let { details ->
                                    TenderPeriodEndData.Bid.Tenderer.Details(
                                        typeOfSupplier = details.typeOfSupplier,
                                        mainEconomicActivities = details.mainEconomicActivities.toList(),
                                        scale = details.scale,
                                        permits = details.permits
                                            .errorIfEmpty {
                                                ErrorException(
                                                    error = ErrorType.IS_EMPTY,
                                                    message = "The bid '${bid.id}' contain empty list of the permits in tenderer '${tenderer.id}'."
                                                )
                                            }
                                            ?.map { permit ->
                                                TenderPeriodEndData.Bid.Tenderer.Details.Permit(
                                                    scheme = permit.scheme,
                                                    id = permit.id,
                                                    url = permit.url,
                                                    details = permit.details.let { details ->
                                                        TenderPeriodEndData.Bid.Tenderer.Details.Permit.Details(
                                                            issuedBy = details.issuedBy.let { issuedBy ->
                                                                TenderPeriodEndData.Bid.Tenderer.Details.Permit.Details.IssuedBy(
                                                                    id = issuedBy.id,
                                                                    name = issuedBy.name
                                                                )
                                                            },
                                                            issuedThought = details.issuedThought.let { issuedThought ->
                                                                TenderPeriodEndData.Bid.Tenderer.Details.Permit.Details.IssuedThought(
                                                                    id = issuedThought.id,
                                                                    name = issuedThought.name
                                                                )
                                                            },
                                                            validityPeriod = details.validityPeriod.let { validityPeriod ->
                                                                TenderPeriodEndData.Bid.Tenderer.Details.Permit.Details.ValidityPeriod(
                                                                    startDate = validityPeriod.startDate,
                                                                    endDate = validityPeriod.endDate
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                            .orEmpty(),
                                        bankAccounts = details.bankAccounts
                                            .errorIfEmpty {
                                                ErrorException(
                                                    error = ErrorType.IS_EMPTY,
                                                    message = "The bid '${bid.id}' contain empty list of the bank accounts in tenderer '${tenderer.id}'."
                                                )
                                            }
                                            ?.map { bankAccount ->
                                                TenderPeriodEndData.Bid.Tenderer.Details.BankAccount(
                                                    description = bankAccount.description,
                                                    bankName = bankAccount.bankName,
                                                    address = bankAccount.address
                                                        .let { address ->
                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.Address(
                                                                streetAddress = address.streetAddress,
                                                                postalCode = address.postalCode,
                                                                details = address.details.let { details ->
                                                                    TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details(
                                                                        country = details.country.let { country ->
                                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details.Country(
                                                                                scheme = country.scheme,
                                                                                id = country.id,
                                                                                description = country.description,
                                                                                uri = country.uri
                                                                            )
                                                                        },
                                                                        region = details.region.let { region ->
                                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details.Region(
                                                                                scheme = region.scheme,
                                                                                id = region.id,
                                                                                description = region.description,
                                                                                uri = region.uri
                                                                            )
                                                                        },
                                                                        locality = details.locality.let { locality ->
                                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details.Locality(
                                                                                scheme = locality.scheme,
                                                                                id = locality.id,
                                                                                description = locality.description,
                                                                                uri = locality.uri
                                                                            )
                                                                        }
                                                                    )
                                                                }
                                                            )
                                                        },
                                                    identifier = bankAccount.identifier
                                                        .let { identifier ->
                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.Identifier(
                                                                scheme = identifier.scheme,
                                                                id = identifier.id
                                                            )
                                                        },
                                                    accountIdentification = bankAccount.accountIdentification
                                                        .let { accountIdentification ->
                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.AccountIdentification(
                                                                scheme = accountIdentification.scheme,
                                                                id = accountIdentification.id
                                                            )
                                                        },
                                                    additionalAccountIdentifiers = bankAccount.additionalAccountIdentifiers
                                                        .errorIfEmpty {
                                                            ErrorException(
                                                                error = ErrorType.IS_EMPTY,
                                                                message = "The bid '${bid.id}' contain empty list of the additional account identifiers in tenderer '${tenderer.id}'."
                                                            )
                                                        }
                                                        ?.map { additionalAccountIdentifier ->
                                                            TenderPeriodEndData.Bid.Tenderer.Details.BankAccount.AdditionalAccountIdentifier(
                                                                scheme = additionalAccountIdentifier.scheme,
                                                                id = additionalAccountIdentifier.id
                                                            )
                                                        }
                                                        .orEmpty()
                                                )
                                            }
                                            .orEmpty(),
                                        legalForm = details.legalForm
                                            ?.let { legalForm ->
                                                TenderPeriodEndData.Bid.Tenderer.Details.LegalForm(
                                                    scheme = legalForm.scheme,
                                                    id = legalForm.id,
                                                    description = legalForm.description,
                                                    uri = legalForm.uri
                                                )
                                            }
                                    )
                                },
                                persons = tenderer.persons
                                    .errorIfEmpty {
                                        ErrorException(
                                            error = ErrorType.IS_EMPTY,
                                            message = "The bid '${bid.id}' contain empty list of the persons in tenderer '${tenderer}'."
                                        )
                                    }
                                    ?.map { person ->
                                        TenderPeriodEndData.Bid.Tenderer.Person(
                                            title = person.title,
                                            name = person.name,
                                            identifier = person.identifier.let { identifier ->
                                                TenderPeriodEndData.Bid.Tenderer.Person.Identifier(
                                                    scheme = identifier.scheme,
                                                    id = identifier.id,
                                                    uri = identifier.uri
                                                )
                                            },
                                            businessFunctions = person.businessFunctions
                                                .mapIfNotEmpty { businessFunction ->
                                                    TenderPeriodEndData.Bid.Tenderer.Person.BusinessFunction(
                                                        id = businessFunction.id,
                                                        type = businessFunction.type,
                                                        jobTitle = businessFunction.jobTitle,
                                                        period = businessFunction.period.let { period ->
                                                            TenderPeriodEndData.Bid.Tenderer.Person.BusinessFunction.Period(
                                                                startDate = period.startDate
                                                            )
                                                        },
                                                        documents = businessFunction.documents
                                                            .errorIfEmpty {
                                                                ErrorException(
                                                                    error = ErrorType.IS_EMPTY,
                                                                    message = "The bid '${bid.id}' contain empty list of the documents in tenderer '${tenderer.id}' person '${person.identifier}' businessFunction '${businessFunction.id}'."
                                                                )
                                                            }
                                                            ?.map { document ->
                                                                TenderPeriodEndData.Bid.Tenderer.Person.BusinessFunction.Document(
                                                                    id = document.id,
                                                                    documentType = document.documentType,
                                                                    title = document.title,
                                                                    description = document.description,
                                                                    datePublished = document.datePublished,
                                                                    url = document.url
                                                                )
                                                            }
                                                            .orEmpty()
                                                    )
                                                }
                                                .orThrow {
                                                    ErrorException(
                                                        error = ErrorType.IS_EMPTY,
                                                        message = "The bid '${bid.id}' contain empty list of the business functions in tenderer '${tenderer.id}' person '${person.identifier}'."
                                                    )
                                                }
                                        )
                                    }
                                    .orEmpty()
                            )
                        }
                        .orThrow {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The bid '${bid.id}' contain empty list of the tenderers."
                            )
                        },
                    value = bid.value,
                    documents = bid.documents
                        .errorIfEmpty {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The bid '${bid.id}' contain empty list of the documents."
                            )
                        }
                        ?.map { document ->
                            TenderPeriodEndData.Bid.Document(
                                id = document.id
                            )
                        }
                        .orEmpty(),
                    relatedLots = bid.relatedLots
                        .mapIfNotEmpty { it }
                        .orThrow {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The bid '${bid.id}' contain empty list of the requirement responses."
                            )
                        },
                    requirementResponses = bid.requirementResponses
                        .errorIfEmpty {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The bid '${bid.id}' contain empty list of the requirement responses."
                            )
                        }
                        ?.map { requirementResponse ->
                            TenderPeriodEndData.Bid.RequirementResponse(
                                id = requirementResponse.id,
                                title = requirementResponse.title,
                                description = requirementResponse.description,
                                value = requirementResponse.value,
                                requirement = requirementResponse.requirement.let { requirement ->
                                    TenderPeriodEndData.Bid.RequirementResponse.Requirement(
                                        id = requirement.id
                                    )
                                },
                                period = requirementResponse.period
                                    ?.let { period ->
                                        TenderPeriodEndData.Bid.RequirementResponse.Period(
                                            startDate = period.startDate,
                                            endDate = period.endDate
                                        )
                                    }
                            )
                        }
                        .orEmpty()
                )
            }
            .orThrow {
                ErrorException(
                    error = ErrorType.IS_EMPTY,
                    message = "The bids is empty."
                )
            },
        criteria = this.criteria
            ?.let { criteria ->
                TenderPeriodEndData.Criteria(
                    id = criteria.id,
                    title = criteria.title,
                    source = criteria.source,
                    description = criteria.description,
                    requirementGroups = criteria.requirementGroups
                        .mapIfNotEmpty { requirementGroup ->
                            TenderPeriodEndData.Criteria.RequirementGroup(
                                id = requirementGroup.id,
                                requirements = requirementGroup.requirements.toList()
                            )
                        }
                        .orThrow {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The criteria '${criteria.id}' contain empty list of the requirement groups."
                            )
                        }
                )
            },
        awards = this.awards
            .mapIfNotEmpty { award ->
                TenderPeriodEndData.Award(
                    id = award.id,
                    date = award.date,
                    status = award.status,
                    statusDetails = award.statusDetails,
                    title = award.title,
                    description = award.description,
                    relatedLots = award.relatedLots.toList(),
                    relatedBid = award.relatedBid,
                    value = award.value,
                    suppliers = award.suppliers
                        .errorIfEmpty {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The award '${award.id}' contain empty list of the suppliers."
                            )
                        }
                        ?.map { supplier ->
                            TenderPeriodEndData.Award.Supplier(
                                id = supplier.id,
                                name = supplier.name
                            )
                        }
                        .orEmpty(),
                    weightedValue = award.weightedValue
                )
            }
            .orThrow {
                ErrorException(
                    error = ErrorType.IS_EMPTY,
                    message = "The awards is empty."
                )
            },
        awardPeriod = this.awardPeriod
            .let { awardPeriod ->
                TenderPeriodEndData.AwardPeriod(
                    startDate = awardPeriod.startDate
                )
            },
        documents = this.documents
            .errorIfEmpty {
                ErrorException(
                    error = ErrorType.IS_EMPTY,
                    message = "The documents is empty."
                )
            }
            ?.map { document ->
                TenderPeriodEndData.Document(
                    documentType = document.documentType,
                    id = document.id,
                    title = document.title,
                    description = document.description,
                    relatedLots = document.relatedLots?.toList() ?: emptyList(),
                    datePublished = document.datePublished,
                    url = document.url
                )
            }
            .orEmpty(),
        unsuccessfulLots = this.unsuccessfulLots
            .errorIfEmpty {
                ErrorException(
                    error = ErrorType.IS_EMPTY,
                    message = "The unsuccessfulLots is empty."
                )
            }
            ?.map { unsuccessfulLot ->
                TenderPeriodEndData.UnsuccessfulLot(
                    id = unsuccessfulLot.id,
                    status = unsuccessfulLot.status
                )
            }
            .orEmpty()
    )
