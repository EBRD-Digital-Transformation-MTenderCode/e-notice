package com.procurement.notice.application.service.auction

import com.procurement.notice.model.ocds.AccountIdentifier
import com.procurement.notice.model.ocds.Address
import com.procurement.notice.model.ocds.AddressDetails
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.BankAccount
import com.procurement.notice.model.ocds.Bid
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.ocds.BusinessFunction
import com.procurement.notice.model.ocds.ContactPoint
import com.procurement.notice.model.ocds.CountryDetails
import com.procurement.notice.model.ocds.Criteria
import com.procurement.notice.model.ocds.Details
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.DocumentBF
import com.procurement.notice.model.ocds.Identifier
import com.procurement.notice.model.ocds.Issue
import com.procurement.notice.model.ocds.LegalForm
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.PermitDetails
import com.procurement.notice.model.ocds.Permits
import com.procurement.notice.model.ocds.Person
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.RequirementGroup
import com.procurement.notice.model.ocds.RequirementReference
import com.procurement.notice.model.ocds.RequirementResponse
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.ocds.toValue
import com.procurement.notice.model.tender.record.ElectronicAuctionModalities
import com.procurement.notice.model.tender.record.ElectronicAuctionProgress
import com.procurement.notice.model.tender.record.ElectronicAuctionProgressBreakdown
import com.procurement.notice.model.tender.record.ElectronicAuctionResult
import com.procurement.notice.model.tender.record.ElectronicAuctions
import com.procurement.notice.model.tender.record.ElectronicAuctionsDetails
import com.procurement.notice.service.ReleaseService
import org.springframework.stereotype.Service

interface AuctionService {
    fun periodEnd(context: AuctionPeriodEndContext, data: AuctionPeriodEndData)
}

@Service
class AuctionServiceImpl(
    private val releaseService: ReleaseService
) : AuctionService {
    override fun periodEnd(context: AuctionPeriodEndContext, data: AuctionPeriodEndData) {
        val entity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val record = releaseService.getRecord(entity.jsonData)

        val updatedAwards = listOf<Award>()

        val updatedBids = updateBids(data)
        val updatedParties = updateParties(parties = record.parties ?: emptyList(), data = data)
        val updatedElectronicAuctions = record.tender.electronicAuctions?.updateElectronicAuctions(data = data)
        val criteria = data.criteria?.convert()

        val updatedRecord = record.copy(
            id = releaseService.newReleaseId(context.ocid), //FR-5.0.1
            date = context.releaseDate,                     //FR-5.0.2
            tag = listOf(Tag.AWARD),                        //FR-5.7.2.6.1
            //FR-5.7.2.6.6
            tender = record.tender.copy(
                statusDetails = TenderStatusDetails.fromValue(data.tenderStatusDetails.value),
                auctionPeriod = data.auctionPeriod
                    .let { auctionPeriod ->
                        Period(
                            startDate = auctionPeriod.startDate,
                            endDate = auctionPeriod.endDate,
                            maxExtentDate = null,
                            durationInDays = null
                        )
                    },
                awardPeriod = Period(
                    startDate = data.awardPeriod.startDate,
                    endDate = null,
                    maxExtentDate = null,
                    durationInDays = null
                ),
                electronicAuctions = updatedElectronicAuctions,
                criteria = if (criteria != null) {
                    record.tender.criteria?.plus(criteria) ?: listOf(criteria)
                } else
                    record.tender.criteria

            ),
            awards = updatedAwards.toHashSet(),             //FR-5.7.2.6.4
            bids = updatedBids,                             //FR-5.7.2.6.3
            parties = updatedParties.toHashSet()            //FR-5.7.2.6.5
        )

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecord,
            publishDate = entity.publishDate
        )
    }

    private fun updateBids(data: AuctionPeriodEndData): Bids {
        val documentsById = data.documents
            .associateBy { it.id }
        return Bids(
            details = data.bids
                .map { bid ->
                    Bid(
                        id = bid.id.toString(),
                        date = bid.date,
                        status = bid.status.value,
                        statusDetails = bid.statusDetails.value,
                        value = bid.value.toValue(),
                        relatedLots = bid.relatedLots
                            .map { it.toString() },
                        requirementResponses = bid.requirementResponses
                            .map { requirementResponse ->
                                RequirementResponse(
                                    id = requirementResponse.id,
                                    title = requirementResponse.title,
                                    description = requirementResponse.description,
                                    value = requirementResponse.value,
                                    period = requirementResponse.period
                                        ?.let { period ->
                                            Period(
                                                startDate = period.startDate,
                                                endDate = period.endDate,
                                                maxExtentDate = null,
                                                durationInDays = null
                                            )
                                        },
                                    requirement = RequirementReference(
                                        id = requirementResponse.requirement.id,
                                        title = null
                                    ),
                                    relatedTenderer = null
                                )
                            }
                            .toHashSet(),
                        tenderers = bid.tenderers
                            .map { tenderer ->
                                OrganizationReference(
                                    id = tenderer.id,
                                    name = tenderer.name,
                                    identifier = null,
                                    additionalIdentifiers = null,
                                    address = null,
                                    contactPoint = null,
                                    details = null,
                                    persones = null,
                                    buyerProfile = null
                                )
                            },
                        documents = bid.documents
                            .map { document ->
                                documentsById.getValue(document.id)
                                    .let {
                                        Document(
                                            id = it.id,
                                            documentType = it.documentType.value,
                                            title = it.title,
                                            description = it.description,
                                            datePublished = it.datePublished,
                                            relatedLots = it.relatedLots
                                                .map { relatedLot -> relatedLot.toString() },
                                            url = it.url,
                                            dateModified = null,
                                            format = null,
                                            language = null,
                                            relatedConfirmations = null
                                        )
                                    }

                            }
                            .toHashSet()
                    )
                }
                .toHashSet(),
            statistics = null
        )
    }

    private fun updateParties(
        parties: Collection<Organization>,
        data: AuctionPeriodEndData
    ): List<Organization> {
        val updatedPartiesByTenderers = updatePartiesByTenderers(parties = parties, data = data)
        return updatePartiesBySuppliers(parties = updatedPartiesByTenderers, data = data)
    }

    private fun updatePartiesByTenderers(
        parties: Collection<Organization>,
        data: AuctionPeriodEndData
    ): List<Organization> {
        val partiesById: Map<String, Organization> = parties.associateBy { it.id!! }
        val tenderersById: Map<String, AuctionPeriodEndData.Bid.Tenderer> = data.bids
            .asSequence()
            .flatMap { bid -> bid.tenderers.asSequence() }
            .associateBy { it.id }

        val tenderersIds = tenderersById.keys
        val partiesIds = partiesById.keys

        val newPartiesByTenderer = newElements(received = tenderersIds, saved = partiesIds)
            .map { id ->
                tenderersById.getValue(id)
                    .convertToParty()
            }

        val updatedPartiesByTenderers = elementsForUpdate(received = tenderersIds, saved = partiesIds)
            .map { id ->
                val party = partiesById.getValue(id)
                if (PartyRole.TENDERER !in party.roles) {
                    val tenderer = tenderersById.getValue(id)
                    party.copy(
                        roles = (party.roles + PartyRole.TENDERER).toHashSet(),
                        additionalIdentifiers = tenderer.additionalIdentifiers
                            .map { additionalIdentifier -> additionalIdentifier.convert() }
                            .toHashSet(),
                        persones = tenderer.persons
                            .map { person -> person.convert() }
                            .toHashSet(),
                        details = tenderer.details
                            .convertToPartiesDetails()

                    )
                } else
                    party

            }

        val immutablePartiesByTenderers = immutableElements(received = tenderersIds, saved = partiesIds)
            .map { id -> partiesById.getValue(id) }

        return newPartiesByTenderer + updatedPartiesByTenderers + immutablePartiesByTenderers
    }

    private fun updatePartiesBySuppliers(
        parties: Collection<Organization>,
        data: AuctionPeriodEndData
    ): List<Organization> {
        val suppliersIds = data.awards
            .asSequence()
            .flatMap { award -> award.suppliers.asSequence() }
            .map { supplier -> supplier.id }
            .toSet()

        return parties.map { party ->
            if (party.id in suppliersIds && PartyRole.SUPPLIER !in party.roles) {
                party.copy(
                    roles = (party.roles + PartyRole.SUPPLIER).toHashSet()
                )
            } else
                party
        }
    }

    private fun AuctionPeriodEndData.Bid.Tenderer.convertToParty() = Organization(
        id = this.id,
        name = this.name,
        identifier = this.identifier
            .let { identifier ->
                Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    legalName = identifier.legalName,
                    uri = identifier.uri
                )
            },
        additionalIdentifiers = this.additionalIdentifiers
            .map { additionalIdentifier -> additionalIdentifier.convert() }
            .toHashSet(),
        address = this.address
            .let { address ->
                Address(
                    streetAddress = address.streetAddress,
                    postalCode = address.postalCode,
                    addressDetails = address.details
                        .let { details ->
                            AddressDetails(
                                country = details.country
                                    .let { country ->
                                        CountryDetails(
                                            scheme = country.scheme,
                                            id = country.id,
                                            description = country.description,
                                            uri = country.uri
                                        )
                                    },
                                region = details.region
                                    .let { region ->
                                        RegionDetails(
                                            scheme = region.scheme,
                                            id = region.id,
                                            description = region.description,
                                            uri = region.uri
                                        )
                                    },
                                locality = details.locality
                                    .let { locality ->
                                        LocalityDetails(
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
        contactPoint = this.contactPoint
            .let { contactPoint ->
                ContactPoint(
                    name = contactPoint.name,
                    email = contactPoint.email,
                    telephone = contactPoint.telephone,
                    faxNumber = contactPoint.faxNumber,
                    url = contactPoint.url
                )
            },
        details = this.details.convertToPartiesDetails(),
        persones = this.persons
            .map { person -> person.convert() }
            .toHashSet(),
        roles = hashSetOf(PartyRole.TENDERER),
        buyerProfile = null
    )

    private fun AuctionPeriodEndData.Bid.Tenderer.AdditionalIdentifier.convert() = Identifier(
        scheme = this.scheme,
        id = this.id,
        legalName = this.legalName,
        uri = this.uri
    )

    private fun AuctionPeriodEndData.Bid.Tenderer.Person.convert() = Person(
        title = this.title,
        name = this.name,
        identifier = this.identifier
            .let { identifier ->
                Identifier(
                    scheme = identifier.scheme,
                    id = identifier.id,
                    uri = identifier.uri,
                    legalName = null
                )
            },
        businessFunctions = this.businessFunctions
            .map { businessFunction ->
                BusinessFunction(
                    id = businessFunction.id,
                    type = businessFunction.type.value,
                    jobTitle = businessFunction.jobTitle,
                    period = Period(
                        startDate = businessFunction.period.startDate,
                        endDate = null,
                        maxExtentDate = null,
                        durationInDays = null
                    ),
                    documents = businessFunction.documents
                        .map { document ->
                            DocumentBF(
                                id = document.id,
                                documentType = document.documentType.value,
                                title = document.title,
                                description = document.description,
                                datePublished = document.datePublished,
                                url = document.url,
                                dateModified = null
                            )
                        }
                )
            }
    )

    private fun AuctionPeriodEndData.Bid.Tenderer.Details.convertToPartiesDetails() = Details(
        typeOfSupplier = this.typeOfSupplier?.value,
        mainEconomicActivities = this.mainEconomicActivities
            .toSet(),
        scale = this.scale.value,
        permits = this.permits
            .map { permit ->
                Permits(
                    scheme = permit.scheme,
                    id = permit.id,
                    url = permit.url,
                    permitDetails = permit.details
                        .let { details ->
                            PermitDetails(
                                issuedBy = details.issuedBy
                                    .let { issuedBy ->
                                        Issue(
                                            id = issuedBy.id,
                                            name = issuedBy.name
                                        )
                                    },
                                issuedThought = details.issuedThought
                                    .let { issuedThought ->
                                        Issue(
                                            id = issuedThought.id,
                                            name = issuedThought.name
                                        )
                                    },
                                validityPeriod = details.validityPeriod
                                    .let { validityPeriod ->
                                        Period(
                                            startDate = validityPeriod.startDate,
                                            endDate = validityPeriod.endDate,
                                            maxExtentDate = null,
                                            durationInDays = null
                                        )
                                    }
                            )
                        }
                )
            },
        bankAccounts = this.bankAccounts
            .map { bankAccount ->
                BankAccount(
                    description = bankAccount.description,
                    bankName = bankAccount.bankName,
                    address = bankAccount.address
                        .let { address ->
                            Address(
                                streetAddress = address.streetAddress,
                                postalCode = address.postalCode,
                                addressDetails = address.details
                                    .let { details ->
                                        AddressDetails(
                                            country = details.country
                                                .let { country ->
                                                    CountryDetails(
                                                        scheme = country.scheme,
                                                        id = country.id,
                                                        description = country.description,
                                                        uri = country.uri
                                                    )
                                                },
                                            region = details.region
                                                .let { region ->
                                                    RegionDetails(
                                                        scheme = region.scheme,
                                                        id = region.id,
                                                        description = region.description,
                                                        uri = region.uri
                                                    )
                                                },
                                            locality = details.locality
                                                .let { locality ->
                                                    LocalityDetails(
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
                            AccountIdentifier(
                                scheme = identifier.scheme,
                                id = identifier.id
                            )
                        },
                    accountIdentification = bankAccount.accountIdentification
                        .let { accountIdentification ->
                            AccountIdentifier(
                                scheme = accountIdentification.scheme,
                                id = accountIdentification.id
                            )
                        },
                    additionalAccountIdentifiers = bankAccount.additionalAccountIdentifiers
                        .map { additionalAccountIdentifier ->
                            AccountIdentifier(
                                scheme = additionalAccountIdentifier.scheme,
                                id = additionalAccountIdentifier.id
                            )
                        }
                        .toSet()
                )
            },
        legalForm = this.legalForm
            ?.let { legalForm ->
                LegalForm(
                    scheme = legalForm.scheme,
                    id = legalForm.id,
                    description = legalForm.description,
                    uri = legalForm.uri
                )
            },
        typeOfBuyer = null,
        mainSectoralActivity = null,
        mainGeneralActivity = null,
        isACentralPurchasingBody = null,
        nutsCode = null
    )

    private fun ElectronicAuctions.updateElectronicAuctions(data: AuctionPeriodEndData): ElectronicAuctions {
        val electronicAuctionsByIds = data.electronicAuctions
            .details
            .associateBy {
                it.id
            }

        return ElectronicAuctions(
            details = this.details
                .asSequence()
                .map { previousDetail ->
                    electronicAuctionsByIds[previousDetail.id!!]
                        ?.let { detail ->
                            ElectronicAuctionsDetails(
                                id = detail.id,
                                relatedLot = detail.relatedLot.toString(),
                                auctionPeriod = detail.auctionPeriod
                                    .let { period ->
                                        Period(
                                            startDate = period.startDate,
                                            endDate = period.endDate,
                                            maxExtentDate = null,
                                            durationInDays = null
                                        )
                                    },
                                electronicAuctionModalities = detail.electronicAuctionModalities
                                    .map { electronicAuctionModality ->
                                        ElectronicAuctionModalities(
                                            eligibleMinimumDifference = electronicAuctionModality.eligibleMinimumDifference.toValue(),
                                            url = electronicAuctionModality.url
                                        )
                                    }
                                    .toSet(),
                                electronicAuctionProgress = detail.electronicAuctionProgress
                                    .map { progress ->
                                        ElectronicAuctionProgress(
                                            id = progress.id,
                                            period = progress.period
                                                .let { period ->
                                                    Period(
                                                        startDate = period.startDate,
                                                        endDate = period.endDate,
                                                        maxExtentDate = null,
                                                        durationInDays = null
                                                    )
                                                },
                                            breakdown = progress.breakdowns
                                                .map { breakdown ->
                                                    ElectronicAuctionProgressBreakdown(
                                                        relatedBid = breakdown.relatedBid.toString(),
                                                        status = breakdown.status,
                                                        dateMet = breakdown.dateMet,
                                                        value = breakdown.value.toValue()
                                                    )
                                                }
                                                .toSet()
                                        )
                                    }
                                    .toSet(),
                                electronicAuctionResult = detail.electronicAuctionResult
                                    .map { result ->
                                        ElectronicAuctionResult(
                                            relatedBid = result.relatedBid.toString(),
                                            value = result.value.toValue()
                                        )
                                    }
                                    .toSet()
                            )
                        }
                        ?: previousDetail
                }
                .toSet()
        )
    }

    private fun AuctionPeriodEndData.Criteria.convert() = Criteria(
        id = this.id,
        title = this.title,
        relatesTo = this.relatedTo?.value,
        relatedItem = this.relatedItem,
        source = this.source,
        description = this.description,
        requirementGroups = this.requirementGroups
            .map { requirementGroup ->
                RequirementGroup(
                    id = requirementGroup.id,
                    description = requirementGroup.description,
                    requirements = requirementGroup.requirements.toList()
                )
            }
    )

    private fun <T> newElements(received: Set<T>, saved: Set<T>) = received.subtract(saved)

    private fun <T> elementsForUpdate(received: Set<T>, saved: Set<T>) = saved.intersect(received)

    private fun <T> immutableElements(received: Set<T>, saved: Set<T>) = saved.subtract(received)
}
