package com.procurement.notice.infrastructure.dto.convert

import com.procurement.notice.application.service.auction.AuctionPeriodEndData
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.infrastructure.dto.auction.periodEnd.request.AuctionPeriodEndRequest
import com.procurement.notice.lib.errorIfEmpty
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.lib.orThrow

fun AuctionPeriodEndRequest.convert(): AuctionPeriodEndData =
    AuctionPeriodEndData(
        tenderStatusDetails = this.tenderStatusDetails,
        bids = this.bids
            .mapIfNotEmpty { bid ->
                AuctionPeriodEndData.Bid(
                    id = bid.id,
                    date = bid.date,
                    status = bid.status,
                    statusDetails = bid.statusDetails,
                    tenderers = bid.tenderers
                        .mapIfNotEmpty { tenderer ->
                            AuctionPeriodEndData.Bid.Tenderer(
                                id = tenderer.id,
                                name = tenderer.name,
                                identifier = tenderer.identifier
                                    .let { identifier ->
                                        AuctionPeriodEndData.Bid.Tenderer.Identifier(
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
                                        AuctionPeriodEndData.Bid.Tenderer.AdditionalIdentifier(
                                            scheme = additionalIdentifier.scheme,
                                            id = additionalIdentifier.id,
                                            legalName = additionalIdentifier.legalName,
                                            uri = additionalIdentifier.uri
                                        )
                                    }
                                    .orEmpty(),
                                address = tenderer.address
                                    .let { address ->
                                        AuctionPeriodEndData.Bid.Tenderer.Address(
                                            streetAddress = address.streetAddress,
                                            postalCode = address.postalCode,
                                            details = address.details
                                                .let { details ->
                                                    AuctionPeriodEndData.Bid.Tenderer.Address.Details(
                                                        country = details.country
                                                            .let { country ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Address.Details.Country(
                                                                    scheme = country.scheme,
                                                                    id = country.id,
                                                                    description = country.description,
                                                                    uri = country.uri
                                                                )
                                                            },
                                                        region = details.region
                                                            .let { region ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Address.Details.Region(
                                                                    scheme = region.scheme,
                                                                    id = region.id,
                                                                    description = region.description,
                                                                    uri = region.uri
                                                                )
                                                            },
                                                        locality = details.locality
                                                            .let { locality ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Address.Details.Locality(
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
                                        AuctionPeriodEndData.Bid.Tenderer.ContactPoint(
                                            name = contactPoint.name,
                                            email = contactPoint.email,
                                            telephone = contactPoint.telephone,
                                            faxNumber = contactPoint.faxNumber,
                                            url = contactPoint.url
                                        )
                                    },
                                details = tenderer.details
                                    .let { details ->
                                        AuctionPeriodEndData.Bid.Tenderer.Details(
                                            typeOfSupplier = details.typeOfSupplier,
                                            mainEconomicActivities = details.mainEconomicActivities
                                                .errorIfEmpty {
                                                    ErrorException(
                                                        error = ErrorType.IS_EMPTY,
                                                        message = "The bid '${bid.id}' contain empty list of the 'mainEconomicActivities' in tenderer '${tenderer.id}'."
                                                    )
                                                }
                                                ?.toList()
                                                .orEmpty(),
                                            scale = details.scale,
                                            permits = details.permits
                                                .errorIfEmpty {
                                                    ErrorException(
                                                        error = ErrorType.IS_EMPTY,
                                                        message = "The bid '${bid.id}' contain empty list of the permits in tenderer '${tenderer.id}'."
                                                    )
                                                }
                                                ?.map { permit ->
                                                    AuctionPeriodEndData.Bid.Tenderer.Details.Permit(
                                                        scheme = permit.scheme,
                                                        id = permit.id,
                                                        url = permit.url,
                                                        details = permit.details
                                                            .let { details ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Details.Permit.Details(
                                                                    issuedBy = details.issuedBy
                                                                        .let { issuedBy ->
                                                                            AuctionPeriodEndData.Bid.Tenderer.Details.Permit.Details.IssuedBy(
                                                                                id = issuedBy.id,
                                                                                name = issuedBy.name
                                                                            )
                                                                        },
                                                                    issuedThought = details.issuedThought
                                                                        .let { issuedThought ->
                                                                            AuctionPeriodEndData.Bid.Tenderer.Details.Permit.Details.IssuedThought(
                                                                                id = issuedThought.id,
                                                                                name = issuedThought.name
                                                                            )
                                                                        },
                                                                    validityPeriod = details.validityPeriod
                                                                        .let { validityPeriod ->
                                                                            AuctionPeriodEndData.Bid.Tenderer.Details.Permit.Details.ValidityPeriod(
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
                                                    AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount(
                                                        description = bankAccount.description,
                                                        bankName = bankAccount.bankName,
                                                        address = bankAccount.address
                                                            .let { address ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.Address(
                                                                    streetAddress = address.streetAddress,
                                                                    postalCode = address.postalCode,
                                                                    details = address.details
                                                                        .let { details ->
                                                                            AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details(
                                                                                country = details.country
                                                                                    .let { country ->
                                                                                        AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details.Country(
                                                                                            scheme = country.scheme,
                                                                                            id = country.id,
                                                                                            description = country.description,
                                                                                            uri = country.uri
                                                                                        )
                                                                                    },
                                                                                region = details.region
                                                                                    .let { region ->
                                                                                        AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details.Region(
                                                                                            scheme = region.scheme,
                                                                                            id = region.id,
                                                                                            description = region.description,
                                                                                            uri = region.uri
                                                                                        )
                                                                                    },
                                                                                locality = details.locality
                                                                                    .let { locality ->
                                                                                        AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.Address.Details.Locality(
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
                                                                AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.Identifier(
                                                                    scheme = identifier.scheme,
                                                                    id = identifier.id
                                                                )
                                                            },
                                                        accountIdentification = bankAccount.accountIdentification
                                                            .let { accountIdentification ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.AccountIdentification(
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
                                                                AuctionPeriodEndData.Bid.Tenderer.Details.BankAccount.AdditionalAccountIdentifier(
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
                                                    AuctionPeriodEndData.Bid.Tenderer.Details.LegalForm(
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
                                        AuctionPeriodEndData.Bid.Tenderer.Person(
                                            title = person.title,
                                            name = person.name,
                                            identifier = person.identifier
                                                .let { identifier ->
                                                    AuctionPeriodEndData.Bid.Tenderer.Person.Identifier(
                                                        scheme = identifier.scheme,
                                                        id = identifier.id,
                                                        uri = identifier.uri
                                                    )
                                                },
                                            businessFunctions = person.businessFunctions
                                                .mapIfNotEmpty { businessFunction ->
                                                    AuctionPeriodEndData.Bid.Tenderer.Person.BusinessFunction(
                                                        id = businessFunction.id,
                                                        type = businessFunction.type,
                                                        jobTitle = businessFunction.jobTitle,
                                                        period = businessFunction.period
                                                            .let { period ->
                                                                AuctionPeriodEndData.Bid.Tenderer.Person.BusinessFunction.Period(
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
                                                                AuctionPeriodEndData.Bid.Tenderer.Person.BusinessFunction.Document(
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
                            AuctionPeriodEndData.Bid.Document(
                                id = document.id
                            )
                        }
                        .orEmpty(),
                    relatedLots = bid.relatedLots
                        .mapIfNotEmpty { it }
                        .orThrow {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The bid '${bid.id}' contain empty list of the related lots."
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
                            AuctionPeriodEndData.Bid.RequirementResponse(
                                id = requirementResponse.id,
                                title = requirementResponse.title,
                                description = requirementResponse.description,
                                value = requirementResponse.value,
                                requirement = requirementResponse.requirement
                                    .let { requirement ->
                                        AuctionPeriodEndData.Bid.RequirementResponse.Requirement(
                                            id = requirement.id
                                        )
                                    },
                                period = requirementResponse.period
                                    ?.let { period ->
                                        AuctionPeriodEndData.Bid.RequirementResponse.Period(
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
                AuctionPeriodEndData.Criteria(
                    id = criteria.id,
                    title = criteria.title,
                    relatedTo = criteria.relatedTo,
                    relatedItem = criteria.relatedItem,
                    source = criteria.source,
                    description = criteria.description,
                    requirementGroups = criteria.requirementGroups
                        .mapIfNotEmpty { requirementGroup ->
                            AuctionPeriodEndData.Criteria.RequirementGroup(
                                id = requirementGroup.id,
                                description = requirementGroup.description,
                                requirements = requirementGroup.requirements
                                    .mapIfNotEmpty { requirement ->
                                        requirement
                                    }
                                    .orThrow {
                                        ErrorException(
                                            error = ErrorType.IS_EMPTY,
                                            message = "The criteria contain empty list of the requirements."
                                        )
                                    }
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
                AuctionPeriodEndData.Award(
                    id = award.id,
                    date = award.date,
                    status = award.status,
                    statusDetails = award.statusDetails,
                    relatedLots = award.relatedLots
                        .mapIfNotEmpty { relatedLot ->
                            relatedLot
                        }
                        .orThrow {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The award contain empty list of the related lots."
                            )
                        },
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
                            AuctionPeriodEndData.Award.Supplier(
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
                AuctionPeriodEndData.AwardPeriod(
                    startDate = awardPeriod.startDate
                )
            },
        auctionPeriod = this.auctionPeriod
            .let { auctionPeriod ->
                AuctionPeriodEndData.AuctionPeriod(
                    startDate = auctionPeriod.startDate,
                    endDate = auctionPeriod.endDate
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
                AuctionPeriodEndData.Document(
                    documentType = document.documentType,
                    id = document.id,
                    title = document.title,
                    description = document.description,
                    relatedLots = document.relatedLots
                        .errorIfEmpty {
                            ErrorException(
                                error = ErrorType.IS_EMPTY,
                                message = "The document contain empty list of the related lots."
                            )
                        }
                        ?.toList()
                        .orEmpty(),
                    datePublished = document.datePublished,
                    url = document.url
                )
            }
            .orEmpty(),
        electronicAuctions = AuctionPeriodEndData.ElectronicAuctions(
            details = this.electronicAuctions.details
                .mapIfNotEmpty { detail ->
                    AuctionPeriodEndData.ElectronicAuctions.Detail(
                        id = detail.id,
                        relatedLot = detail.relatedLot,
                        auctionPeriod = detail.auctionPeriod
                            .let { auctionPeriod ->
                                AuctionPeriodEndData.ElectronicAuctions.Detail.AuctionPeriod(
                                    startDate = auctionPeriod.startDate,
                                    endDate = auctionPeriod.endDate
                                )
                            },
                        electronicAuctionModalities = detail.electronicAuctionModalities
                            .mapIfNotEmpty { electronicAuctionModality ->
                                AuctionPeriodEndData.ElectronicAuctions.Detail.ElectronicAuctionModality(
                                    eligibleMinimumDifference = electronicAuctionModality.eligibleMinimumDifference,
                                    url = electronicAuctionModality.url
                                )
                            }
                            .orThrow {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The 'electronicAuctions' contain empty list of the 'electronicAuctionModalities'."
                                )
                            },
                        electronicAuctionProgress = detail.electronicAuctionProgress
                            .mapIfNotEmpty { progress ->
                                AuctionPeriodEndData.ElectronicAuctions.Detail.ElectronicAuctionProgres(
                                    id = progress.id,
                                    period = progress.period
                                        .let { period ->
                                            AuctionPeriodEndData.ElectronicAuctions.Detail.ElectronicAuctionProgres.Period(
                                                startDate = period.startDate,
                                                endDate = period.endDate
                                            )
                                        },
                                    breakdowns = progress.breakdowns
                                        .mapIfNotEmpty { breakdown ->
                                            AuctionPeriodEndData.ElectronicAuctions.Detail.ElectronicAuctionProgres.Breakdown(
                                                relatedBid = breakdown.relatedBid,
                                                status = breakdown.status,
                                                dateMet = breakdown.dateMet,
                                                value = breakdown.value
                                            )
                                        }
                                        .orThrow {
                                            ErrorException(
                                                error = ErrorType.IS_EMPTY,
                                                message = "The 'electronicAuctionProgress' contain empty list of the 'breakdown'."
                                            )
                                        }
                                )
                            }
                            .orThrow {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The 'electronicAuctions' contain empty list of the 'electronicAuctionProgress'."
                                )
                            },
                        electronicAuctionResult = detail.electronicAuctionResult
                            .mapIfNotEmpty { result ->
                                AuctionPeriodEndData.ElectronicAuctions.Detail.ElectronicAuctionResult(
                                    relatedBid = result.relatedBid,
                                    value = result.value
                                )
                            }
                            .orThrow {
                                ErrorException(
                                    error = ErrorType.IS_EMPTY,
                                    message = "The 'electronicAuctions' contain empty list of the 'electronicAuctionResult'."
                                )
                            }
                    )
                }
                .orThrow {
                    ErrorException(
                        error = ErrorType.IS_EMPTY,
                        message = "The 'electronicAuctions' contain empty list of the 'details'."
                    )
                }
        )
    )
