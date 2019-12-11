package com.procurement.notice.service

import com.fasterxml.jackson.databind.JsonNode
import com.procurement.notice.application.service.award.auction.StartAwardPeriodAuctionContext
import com.procurement.notice.application.service.award.auction.StartAwardPeriodAuctionData
import com.procurement.notice.application.service.award.auction.StartAwardPeriodAuctionResult
import com.procurement.notice.application.service.tender.periodEnd.TenderPeriodEndContext
import com.procurement.notice.application.service.tender.periodEnd.TenderPeriodEndData
import com.procurement.notice.application.service.tender.periodEnd.TenderPeriodEndResult
import com.procurement.notice.application.service.tender.unsuccessful.TenderUnsuccessfulContext
import com.procurement.notice.application.service.tender.unsuccessful.TenderUnsuccessfulData
import com.procurement.notice.application.service.tender.unsuccessful.TenderUnsuccessfulResult
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.lib.mapIfNotEmpty
import com.procurement.notice.model.bpe.DataResponseDto
import com.procurement.notice.model.bpe.ResponseDto
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
import com.procurement.notice.model.ocds.InitiationType
import com.procurement.notice.model.ocds.Issue
import com.procurement.notice.model.ocds.LegalForm
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.PermitDetails
import com.procurement.notice.model.ocds.Permits
import com.procurement.notice.model.ocds.Person
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.RelatedProcessType
import com.procurement.notice.model.ocds.RequirementGroup
import com.procurement.notice.model.ocds.RequirementReference
import com.procurement.notice.model.ocds.RequirementResponse
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.TenderDescription
import com.procurement.notice.model.ocds.TenderStatus
import com.procurement.notice.model.ocds.TenderStatusDetails
import com.procurement.notice.model.ocds.TenderTitle
import com.procurement.notice.model.ocds.toValue
import com.procurement.notice.model.tender.dto.AuctionPeriodEndDto
import com.procurement.notice.model.tender.dto.AwardByBidDto
import com.procurement.notice.model.tender.dto.AwardPeriodEndDto
import com.procurement.notice.model.tender.dto.StandstillPeriodEndDto
import com.procurement.notice.model.tender.dto.StartNewStageDto
import com.procurement.notice.model.tender.dto.TenderStatusDto
import com.procurement.notice.model.tender.dto.UnsuspendTenderDto
import com.procurement.notice.model.tender.enquiry.RecordEnquiry
import com.procurement.notice.model.tender.record.ElectronicAuctionModalities
import com.procurement.notice.model.tender.record.ElectronicAuctions
import com.procurement.notice.model.tender.record.ElectronicAuctionsDetails
import com.procurement.notice.model.tender.record.Record
import com.procurement.notice.model.tender.record.RecordTender
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toJson
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class TenderService(
    private val releaseService: ReleaseService,
    private val organizationService: OrganizationService,
    private val relatedProcessService: RelatedProcessService
) {

    fun tenderPeriodEnd(
        context: TenderPeriodEndContext,
        data: TenderPeriodEndData
    ): TenderPeriodEndResult {
        val recordEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        val updatedAwards = updateAwards(data = data, awards = record.awards)
        val updatedLots = updateLots(data = data, lots = record.tender.lots)

        val newCriteria = data.criteria
            ?.let { criteria ->
                Criteria(
                    id = criteria.id,
                    title = criteria.title,
                    description = criteria.description,
                    source = criteria.source,
                    relatedItem = null,
                    relatesTo = null,
                    requirementGroups = criteria.requirementGroups.map { requirementGroup ->
                        RequirementGroup(
                            id = requirementGroup.id,
                            description = null,
                            requirements = requirementGroup.requirements
                        )
                    }
                )
            }

        val updatedCriteria: List<Criteria>? = if (newCriteria != null)
            (record.tender.criteria ?: emptyList()) + newCriteria
        else
            record.tender.criteria

        //FR-5.7.2.1.3
        val updatedBids = updateBids(data = data, bids = record.bids)

        //FR-5.7.2.1.5
        val updatedParties = updateParties(data = data, parties = record.parties ?: emptyList())

        val updatedRecord = record.copy(
            id = releaseService.newReleaseId(context.ocid), //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.AWARD), //FR-5.7.2.1.1
            tender = record.tender.copy(
                //FR-5.7.2.1.6
                awardPeriod = Period(
                    startDate = data.awardPeriod.startDate,
                    endDate = null,
                    maxExtentDate = null,
                    durationInDays = null
                ),
                statusDetails = TenderStatusDetails.fromValue(data.tenderStatusDetails.value), //FR-5.7.2.1.6
                lots = updatedLots?.toHashSet(), //FR-5.7.2.1.6
                criteria = updatedCriteria //FR-5.7.2.1.6
            ),
            awards = updatedAwards?.toHashSet(),
            bids = updatedBids, //FR-5.7.2.1.3
            parties = updatedParties.toHashSet() //FR-5.7.2.1.5
        )
        organizationService.processRecordPartiesFromBids(updatedRecord)
        organizationService.processRecordPartiesFromAwards(updatedRecord)
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecord,
            publishDate = recordEntity.publishDate
        )
        return TenderPeriodEndResult(cpid = context.cpid, ocid = context.ocid)
    }

    /**
     * FR-5.7.2.1.4
     * eNotice Get.Awards list from Request and saves them as release.awards list in new Release:
     */
    private fun updateAwards(data: TenderPeriodEndData, awards: Collection<Award>?): Collection<Award>? =
        if (data.awards.isNotEmpty())
            data.awards.map { award ->
                Award(
                    id = award.id,
                    date = award.date,
                    status = award.status.value,
                    statusDetails = award.statusDetails.value,
                    title = award.title,
                    description = award.description,
                    relatedLots = award.relatedLots.map { it.toString() },
                    relatedBid = award.relatedBid,
                    value = award.value?.toValue(),
                    suppliers = award.suppliers.map { supplier ->
                        OrganizationReference(
                            id = supplier.id,
                            name = supplier.name,
                            additionalIdentifiers = null,
                            identifier = null,
                            address = null,
                            contactPoint = null,
                            details = null,
                            buyerProfile = null,
                            persones = null
                        )
                    },
                    weightedValue = award.weightedValue?.toValue(),
                    amendment = null,
                    amendments = null,
                    items = null,
                    contractPeriod = null,
                    documents = null,
                    requirementResponses = null,
                    reviewProceedings = null
                )
            }
        else
            awards

    /**
     * FR-5.7.2.1.6
     * 4. forEach unsuccessfulLots object from Request (if they were transferred):
     *   a. Finds in new Release tender.lots object where lots.ID == unsuccessfulLots.ID;
     *   b. Changes value of lots.status in lot (found before) getting value from unsuccessfulLots object of request;
     */
    private fun updateLots(data: TenderPeriodEndData, lots: Collection<Lot>?): Collection<Lot>? =
        if (data.unsuccessfulLots.isNotEmpty()) {
            val unsuccessfulLotsById = data.unsuccessfulLots.associateBy { it.id.toString() }
            lots!!.map { lot ->
                if (lot.id in unsuccessfulLotsById)
                    lot.copy(
                        status = unsuccessfulLotsById.getValue(lot.id).status.value
                    )
                else
                    lot
            }
        } else
            lots

    /**
     * FR-5.7.2.1.3
     *
     * 1. Get.Bids from Request and saves them as a release.bids.details array according to next order:
     *   a. Saves bids.ID from request as bids.details.ID;
     *   b. Saves bids.value from request as bids.details.value;
     *   c. Saves bids.requirementResponses from request as bids.details.requirementResponses;
     *   d. Saves bids.date from request as bids.details.date;
     *   e. Saves bids.relatedLots from request as bids.details.relatedLots;
     *   f. Saves bids.status from request as bids.details.status;
     *   g. Saves bids.statusDetails from request as bids.details.statusDetails;
     *   h. forEach bids.tenderers object from Request:
     *     i.  Saves bids.tenderers.ID from request as bids.details.tenderers.ID;
     *     ii. Saves bids.tenderers.name from request as bids.details.tenderers.name;
     *   i. forEach bids.documents object finds appropriate documents object from Request
     *      (where bid.documents.ID == documents.ID from Request) and saves it in bids.documents array
     *      instead of document with same identifier;
     */
    private fun updateBids(data: TenderPeriodEndData, bids: Bids?): Bids? {
        return if (data.bids.isNotEmpty()) {
            val bidsDocumentsById = data.documents.associateBy { it.id }
            val details = data.bids
                .map { bid ->
                    Bid(
                        id = bid.id, //FR-5.7.2.1.3
                        date = bid.date, //FR-5.7.2.1.3
                        status = bid.status.value, //FR-5.7.2.1.3
                        statusDetails = bid.statusDetails.value, //FR-5.7.2.1.3
                        tenderers = bid.tenderers
                            .map { tenderer ->
                                OrganizationReference(
                                    id = tenderer.id, //FR-5.7.2.1.3
                                    name = tenderer.name, //FR-5.7.2.1.3
                                    identifier = tenderer.identifier //TODO need?
                                        .let { identifier ->
                                            Identifier(
                                                scheme = identifier.scheme,
                                                id = identifier.id,
                                                legalName = identifier.legalName,
                                                uri = identifier.uri
                                            )
                                        },
                                    additionalIdentifiers = tenderer.additionalIdentifiers
                                        .map { additionalIdentifier ->
                                            Identifier(
                                                scheme = additionalIdentifier.scheme,
                                                id = additionalIdentifier.id,
                                                legalName = additionalIdentifier.legalName,
                                                uri = additionalIdentifier.uri
                                            )
                                        }.toHashSet(),
                                    address = tenderer.address
                                        .let { address ->
                                            Address(
                                                streetAddress = address.streetAddress,
                                                postalCode = address.postalCode,
                                                addressDetails = address.details.let { details ->
                                                    AddressDetails(
                                                        country = details.country.let { country ->
                                                            CountryDetails(
                                                                scheme = country.scheme,
                                                                id = country.id,
                                                                description = country.description,
                                                                uri = country.uri
                                                            )
                                                        },
                                                        region = details.region.let { region ->
                                                            RegionDetails(
                                                                scheme = region.scheme,
                                                                id = region.id,
                                                                description = region.description,
                                                                uri = region.uri
                                                            )
                                                        },
                                                        locality = details.locality.let { locality ->
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
                                    contactPoint = tenderer.contactPoint
                                        .let { contactPoint ->
                                            ContactPoint(
                                                name = contactPoint.name,
                                                email = contactPoint.email,
                                                telephone = contactPoint.telephone,
                                                faxNumber = contactPoint.faxNumber,
                                                url = contactPoint.url
                                            )
                                        },
                                    details = tenderer.details
                                        .let { details ->
                                            Details(
                                                typeOfSupplier = details.typeOfSupplier.value,
                                                mainEconomicActivities = details.mainEconomicActivities.toSet(),
                                                scale = details.scale.value,
                                                permits = details.permits
                                                    .map { permit ->
                                                        Permits(
                                                            scheme = permit.scheme,
                                                            id = permit.id,
                                                            url = permit.url,
                                                            permitDetails = permit.details.let { details ->
                                                                PermitDetails(
                                                                    issuedBy = details.issuedBy.let { issuedBy ->
                                                                        Issue(
                                                                            id = issuedBy.id,
                                                                            name = issuedBy.name
                                                                        )
                                                                    },
                                                                    issuedThought = details.issuedThought.let { issuedThought ->
                                                                        Issue(
                                                                            id = issuedThought.id,
                                                                            name = issuedThought.name
                                                                        )
                                                                    },
                                                                    validityPeriod = details.validityPeriod.let { validityPeriod ->
                                                                        Period(
                                                                            startDate = validityPeriod.startDate,
                                                                            endDate = validityPeriod.endDate,
                                                                            durationInDays = null,
                                                                            maxExtentDate = null
                                                                        )
                                                                    }
                                                                )
                                                            }
                                                        )
                                                    },
                                                bankAccounts = details.bankAccounts
                                                    .map { bankAccount ->
                                                        BankAccount(
                                                            description = bankAccount.description,
                                                            bankName = bankAccount.bankName,
                                                            address = bankAccount.address
                                                                .let { address ->
                                                                    Address(
                                                                        streetAddress = address.streetAddress,
                                                                        postalCode = address.postalCode,
                                                                        addressDetails = address.details.let { details ->
                                                                            AddressDetails(
                                                                                country = details.country.let { country ->
                                                                                    CountryDetails(
                                                                                        scheme = country.scheme,
                                                                                        id = country.id,
                                                                                        description = country.description,
                                                                                        uri = country.uri
                                                                                    )
                                                                                },
                                                                                region = details.region.let { region ->
                                                                                    RegionDetails(
                                                                                        scheme = region.scheme,
                                                                                        id = region.id,
                                                                                        description = region.description,
                                                                                        uri = region.uri
                                                                                    )
                                                                                },
                                                                                locality = details.locality.let { locality ->
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
                                                legalForm = details.legalForm
                                                    ?.let { legalForm ->
                                                        LegalForm(
                                                            scheme = legalForm.scheme,
                                                            id = legalForm.id,
                                                            description = legalForm.description,
                                                            uri = legalForm.uri
                                                        )
                                                    },
                                                typeOfBuyer = null,
                                                mainGeneralActivity = null,
                                                mainSectoralActivity = null,
                                                isACentralPurchasingBody = null,
                                                nutsCode = null
                                            )
                                        },
                                    persones = tenderer.persons
                                        .map { person ->
                                            Person(
                                                title = person.title,
                                                name = person.name,
                                                identifier = person.identifier
                                                    .let { identifier ->
                                                        Identifier(
                                                            scheme = identifier.scheme,
                                                            id = identifier.id,
                                                            uri = identifier.uri,
                                                            legalName = null
                                                        )
                                                    },
                                                businessFunctions = person.businessFunctions
                                                    .map { businessFunction ->
                                                        BusinessFunction(
                                                            id = businessFunction.id,
                                                            type = businessFunction.type.value,
                                                            jobTitle = businessFunction.jobTitle,
                                                            period = Period(
                                                                startDate = businessFunction.period.startDate,
                                                                endDate = null,
                                                                durationInDays = null,
                                                                maxExtentDate = null
                                                            ),
                                                            documents = businessFunction.documents
                                                                .map { document ->
                                                                    DocumentBF(
                                                                        id = document.id,
                                                                        documentType = document.documentType.value,
                                                                        title = document.title,
                                                                        description = document.description,
                                                                        dateModified = null,
                                                                        url = null,
                                                                        datePublished = null
                                                                    )
                                                                }
                                                        )
                                                    }
                                            )
                                        }.toHashSet(),
                                    buyerProfile = null
                                )
                            },
                        //FR-5.7.2.1.3
                        relatedLots = bid.relatedLots
                            .map { it.toString() },
                        value = bid.value.toValue(), //FR-5.7.2.1.3
                        //FR-5.7.2.1.3
                        documents = bid.documents
                            .map { document ->
                                bidsDocumentsById.getValue(document.id)
                                    .let {
                                        Document(
                                            documentType = it.documentType.value,
                                            id = it.id,
                                            title = it.title,
                                            description = it.description,
                                            relatedLots = it.relatedLots.map { relatedLot -> relatedLot.toString() },
                                            datePublished = it.datePublished,
                                            url = it.url,
                                            dateModified = null,
                                            format = null,
                                            language = null,
                                            relatedConfirmations = null
                                        )
                                    }
                            }
                            .toHashSet(),
                        //FR-5.7.2.1.3
                        requirementResponses = bid.requirementResponses
                            .map { requirementResponse ->
                                RequirementResponse(
                                    id = requirementResponse.id,
                                    title = requirementResponse.title,
                                    description = requirementResponse.description,
                                    value = requirementResponse.value,
                                    requirement = RequirementReference(
                                        id = requirementResponse.requirement.id,
                                        title = null
                                    ),
                                    period = requirementResponse.period
                                        ?.let { period ->
                                            Period(
                                                startDate = period.startDate,
                                                endDate = period.endDate,
                                                durationInDays = null,
                                                maxExtentDate = null
                                            )
                                        },
                                    relatedTenderer = null
                                )
                            }
                            .toHashSet()
                    )
                }
            Bids(statistics = null, details = details.toHashSet())
        } else
            bids
    }

    /**
     * FR-5.7.2.1.5
     *
     * 1. Rewrites release.parties from current actual Release to new one.
     * 2. forEach bids.tenderers object from Request:
     *   a. Get.tenderers.ID and checks availability of organization object in Parties list
     *      where parties.ID == tenderers.ID;
     *   b. IF [there is NO such organization] then:
     *     i.  Systems saves processed tenderers object in Parties list;
     *     ii. Sets for saved organization parties.roles == "tenderer";
     *   c. ELSE [there is organization with same ID] { then:
     *     i. Checks availability of parties.roles == "tenderer" in this Parties object:
     *       1. IF [organization has such role] then: system processes next tenderer;
     *       2. ELSE [no such roles in parties object] { then:
     *         a. Adds to this organization parties.roles == "tenderer";
     *         b. Adds to this organization (instead of existed) next arrays and objects
     *            from tenderers object of Request:
     *           i.   Saves tenderers.persones as parties.persones;
     *           ii.  Saves tenderers.details as parties.details;
     *           iii. Saves tenderers.additionalIdentifiers as parties.additionalIdentifiers;
     * 3. forEach awards.suppliers object from Request:
     *   a. Get.supplier.ID and finds organization object in Parties list where parties.ID == supplier.ID;
     *   b. Checks availability of parties.roles == "supplier" in this Parties object:
     *     i.  IF [organization has such role] then: system processes next supplier;
     *     ii. ELSE [no such roles in parties object] then system adds to this organization parties.roles == "supplier";
     */
    private fun updateParties(
        data: TenderPeriodEndData,
        parties: Collection<Organization>
    ): Collection<Organization> {
        val tenderersById: Map<String, TenderPeriodEndData.Bid.Tenderer> = data.bids
            .asSequence()
            .flatMap {
                it.tenderers.asSequence()
            }
            .associateBy {
                it.id
            }

        val partiesById: Map<String, Organization> = parties.associateBy { it.id!! }

        val tenderersIds = tenderersById.keys
        val partiesIds = partiesById.keys
        val idsNewParties = getNewElements(tenderersIds, partiesIds)
        val idsUpdatingParties = getElementsForUpdate(tenderersIds, partiesIds)

        val updatedParties: Sequence<Organization> = parties.asSequence()
            .map { party ->
                val partyId = party.id!!
                if (partyId in idsUpdatingParties) {
                    if (PartyRole.TENDERER !in party.roles) {
                        val tenderer = tenderersById.getValue(partyId)
                        party.copy(
                            roles = (party.roles + PartyRole.TENDERER).toHashSet(),
                            persones = tenderer.persons
                                .mapIfNotEmpty { person ->
                                    Person(
                                        title = person.title,
                                        name = person.name,
                                        identifier = person.identifier
                                            .let { identifier ->
                                                Identifier(
                                                    scheme = identifier.scheme,
                                                    id = identifier.id,
                                                    uri = identifier.uri,
                                                    legalName = null
                                                )
                                            },
                                        businessFunctions = person.businessFunctions
                                            .map { businessFunction ->
                                                BusinessFunction(
                                                    id = businessFunction.id,
                                                    type = businessFunction.type.value,
                                                    jobTitle = businessFunction.jobTitle,
                                                    period = Period(
                                                        startDate = businessFunction.period.startDate,
                                                        endDate = null,
                                                        durationInDays = null,
                                                        maxExtentDate = null
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
                                }
                                ?.toHashSet()
                                ?: party.persones,
                            details = tenderer.details
                                .let { details ->
                                    Details(
                                        typeOfSupplier = details.typeOfSupplier.value,
                                        mainEconomicActivities = details.mainEconomicActivities.toSet(),
                                        scale = details.scale.value,
                                        permits = details.permits
                                            .map { permit ->
                                                Permits(
                                                    scheme = permit.scheme,
                                                    id = permit.id,
                                                    url = permit.url,
                                                    permitDetails = permit.details.let { details ->
                                                        PermitDetails(
                                                            issuedBy = details.issuedBy.let { issuedBy ->
                                                                Issue(
                                                                    id = issuedBy.id,
                                                                    name = issuedBy.name
                                                                )
                                                            },
                                                            issuedThought = details.issuedThought.let { issuedThought ->
                                                                Issue(
                                                                    id = issuedThought.id,
                                                                    name = issuedThought.name
                                                                )
                                                            },
                                                            validityPeriod = details.validityPeriod.let { validityPeriod ->
                                                                Period(
                                                                    startDate = validityPeriod.startDate,
                                                                    endDate = validityPeriod.endDate,
                                                                    durationInDays = null,
                                                                    maxExtentDate = null
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            },
                                        bankAccounts = details.bankAccounts
                                            .map { bankAccount ->
                                                BankAccount(
                                                    description = bankAccount.description,
                                                    bankName = bankAccount.bankName,
                                                    address = bankAccount.address
                                                        .let { address ->
                                                            Address(
                                                                streetAddress = address.streetAddress,
                                                                postalCode = address.postalCode,
                                                                addressDetails = address.details.let { details ->
                                                                    AddressDetails(
                                                                        country = details.country.let { country ->
                                                                            CountryDetails(
                                                                                scheme = country.scheme,
                                                                                id = country.id,
                                                                                description = country.description,
                                                                                uri = country.uri
                                                                            )
                                                                        },
                                                                        region = details.region.let { region ->
                                                                            RegionDetails(
                                                                                scheme = region.scheme,
                                                                                id = region.id,
                                                                                description = region.description,
                                                                                uri = region.uri
                                                                            )
                                                                        },
                                                                        locality = details.locality.let { locality ->
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
                                        legalForm = details.legalForm
                                            ?.let { legalForm ->
                                                LegalForm(
                                                    scheme = legalForm.scheme,
                                                    id = legalForm.id,
                                                    description = legalForm.description,
                                                    uri = legalForm.uri
                                                )
                                            },
                                        isACentralPurchasingBody = null,
                                        mainGeneralActivity = null,
                                        mainSectoralActivity = null,
                                        typeOfBuyer = null,
                                        nutsCode = null
                                    )
                                },
                            additionalIdentifiers = tenderer.additionalIdentifiers
                                .mapIfNotEmpty { additionalIdentifier ->
                                    Identifier(
                                        scheme = additionalIdentifier.scheme,
                                        id = additionalIdentifier.id,
                                        legalName = additionalIdentifier.legalName,
                                        uri = additionalIdentifier.uri
                                    )
                                }
                                ?.toHashSet()
                                ?: party.additionalIdentifiers
                        )
                    } else
                        party
                } else
                    party
            }

        val newParties: Sequence<Organization> = idsNewParties.asSequence()
            .map { id ->
                val tenderer = tenderersById.getValue(id)
                Organization(
                    id = tenderer.id,
                    name = tenderer.name,
                    roles = hashSetOf(PartyRole.TENDERER),
                    identifier = tenderer.identifier
                        .let { identifier ->
                            Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                uri = identifier.uri,
                                legalName = null
                            )
                        },
                    additionalIdentifiers = tenderer.additionalIdentifiers
                        .asSequence()
                        .map { identifier ->
                            Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                uri = identifier.uri,
                                legalName = null
                            )
                        }
                        .toHashSet(),
                    address = tenderer.address.let { address ->
                        Address(
                            streetAddress = address.streetAddress,
                            postalCode = address.postalCode,
                            addressDetails = address.details.let { details ->
                                AddressDetails(
                                    country = details.country.let { country ->
                                        CountryDetails(
                                            scheme = country.scheme,
                                            id = country.id,
                                            description = country.description,
                                            uri = country.uri
                                        )
                                    },
                                    region = details.region.let { region ->
                                        RegionDetails(
                                            scheme = region.scheme,
                                            id = region.id,
                                            description = region.description,
                                            uri = region.uri
                                        )
                                    },
                                    locality = details.locality.let { locality ->
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
                    contactPoint = tenderer.contactPoint.let { contactPoint ->
                        ContactPoint(
                            name = contactPoint.name,
                            email = contactPoint.email,
                            telephone = contactPoint.telephone,
                            faxNumber = contactPoint.faxNumber,
                            url = contactPoint.url
                        )
                    },
                    persones = tenderer.persons
                        .map { person ->
                            Person(
                                title = person.title,
                                name = person.name,
                                identifier = person.identifier
                                    .let { identifier ->
                                        Identifier(
                                            scheme = identifier.scheme,
                                            id = identifier.id,
                                            uri = identifier.uri,
                                            legalName = null
                                        )
                                    },
                                businessFunctions = person.businessFunctions
                                    .map { businessFunction ->
                                        BusinessFunction(
                                            id = businessFunction.id,
                                            type = businessFunction.type.value,
                                            jobTitle = businessFunction.jobTitle,
                                            period = Period(
                                                startDate = businessFunction.period.startDate,
                                                endDate = null,
                                                durationInDays = null,
                                                maxExtentDate = null
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
                        }
                        .toHashSet(),
                    details = tenderer.details.let { details ->
                        Details(
                            typeOfSupplier = details.typeOfSupplier.value,
                            mainEconomicActivities = details.mainEconomicActivities.toSet(),
                            scale = details.scale.value,
                            permits = details.permits
                                .map { permit ->
                                    Permits(
                                        scheme = permit.scheme,
                                        id = permit.id,
                                        url = permit.url,
                                        permitDetails = permit.details.let { details ->
                                            PermitDetails(
                                                issuedBy = details.issuedBy.let { issuedBy ->
                                                    Issue(
                                                        id = issuedBy.id,
                                                        name = issuedBy.name
                                                    )
                                                },
                                                issuedThought = details.issuedThought.let { issuedThought ->
                                                    Issue(
                                                        id = issuedThought.id,
                                                        name = issuedThought.name
                                                    )
                                                },
                                                validityPeriod = details.validityPeriod.let { validityPeriod ->
                                                    Period(
                                                        startDate = validityPeriod.startDate,
                                                        endDate = validityPeriod.endDate,
                                                        durationInDays = null,
                                                        maxExtentDate = null
                                                    )
                                                }
                                            )
                                        }
                                    )
                                },
                            bankAccounts = details.bankAccounts
                                .map { bankAccount ->
                                    BankAccount(
                                        description = bankAccount.description,
                                        bankName = bankAccount.bankName,
                                        address = bankAccount.address
                                            .let { address ->
                                                Address(
                                                    streetAddress = address.streetAddress,
                                                    postalCode = address.postalCode,
                                                    addressDetails = address.details.let { details ->
                                                        AddressDetails(
                                                            country = details.country.let { country ->
                                                                CountryDetails(
                                                                    scheme = country.scheme,
                                                                    id = country.id,
                                                                    description = country.description,
                                                                    uri = country.uri
                                                                )
                                                            },
                                                            region = details.region.let { region ->
                                                                RegionDetails(
                                                                    scheme = region.scheme,
                                                                    id = region.id,
                                                                    description = region.description,
                                                                    uri = region.uri
                                                                )
                                                            },
                                                            locality = details.locality.let { locality ->
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
                            legalForm = details.legalForm
                                ?.let { legalForm ->
                                    LegalForm(
                                        scheme = legalForm.scheme,
                                        id = legalForm.id,
                                        description = legalForm.description,
                                        uri = legalForm.uri
                                    )
                                },
                            isACentralPurchasingBody = null,
                            mainGeneralActivity = null,
                            mainSectoralActivity = null,
                            typeOfBuyer = null,
                            nutsCode = null
                        )
                    },
                    buyerProfile = null
                )
            }

        val suppliersIds = data.awards
            .asSequence()
            .flatMap { award ->
                award.suppliers.asSequence()
            }
            .map { supplier ->
                supplier.id
            }
            .toSet()

        return (updatedParties + newParties)
            .map { party ->
                if (party.id in suppliersIds && PartyRole.SUPPLIER !in party.roles)
                    party.copy(
                        roles = (party.roles + PartyRole.SUPPLIER).toHashSet()
                    )
                else
                    party
            }
            .toList()
    }

    fun tenderPeriodEndAuction(
        data: StartAwardPeriodAuctionData,
        context: StartAwardPeriodAuctionContext
    ): StartAwardPeriodAuctionResult {
        val recordEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val updatedLots = setUnsuccessfulStatusToLots(data, record)
        val updatedElectronicAuctions = getUpdatedElectronicAuctions(data, record)
        val updatedRecord = record.copy(
            id = releaseService.newReleaseId(context.ocid),
            date = context.startDate,
            tag = listOf(Tag.AWARD),
            awards = data.awards
                .asSequence()
                .map { award ->
                    Award(
                        id = award.id.toString(),
                        status = award.status.value,
                        statusDetails = award.statusDetails.value,
                        relatedLots = award.relatedLots.map { it.toString() },
                        date = award.date,
                        description = award.description,
                        title = award.title,
                        weightedValue = null,
                        items = null,
                        documents = null,
                        suppliers = null,
                        relatedBid = null,
                        value = null,
                        requirementResponses = null,
                        amendment = null,
                        amendments = null,
                        contractPeriod = null,
                        reviewProceedings = null

                    )
                }
                .toHashSet()
                .ifEmpty { record.awards },
            tender = record.tender.copy(
                statusDetails = TenderStatusDetails.fromValue(data.tenderStatusDetails.value),
                lots = updatedLots?.toHashSet(),
                electronicAuctions = updatedElectronicAuctions
            )
        )
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecord,
            publishDate = recordEntity.publishDate
        )
        return StartAwardPeriodAuctionResult(
            cpid = context.cpid,
            ocid = context.ocid
        )
    }

    private fun getUpdatedElectronicAuctions(
        data: StartAwardPeriodAuctionData,
        record: Record
    ): ElectronicAuctions? {
        val requestAuctionsById = data.electronicAuctions.details.associateBy { it.id }

        return record.tender.electronicAuctions.let { recordAuctions ->
            recordAuctions?.copy(
                details = recordAuctions.details
                    .asSequence()
                    .map { detail ->
                        val id = detail.id!!
                        val requestAuction = requestAuctionsById[id]
                        if (requestAuction != null) {
                            ElectronicAuctionsDetails(
                                id = requestAuction.id,
                                auctionPeriod = requestAuction.auctionPeriod
                                    .let { auctionPeriod ->
                                        Period(
                                            startDate = auctionPeriod.startDate,
                                            endDate = null,
                                            durationInDays = null,
                                            maxExtentDate = null
                                        )
                                    },
                                electronicAuctionModalities = requestAuction.electronicAuctionModalities
                                    .asSequence()
                                    .map { modality ->
                                        ElectronicAuctionModalities(
                                            url = modality.url,
                                            eligibleMinimumDifference = modality.eligibleMinimumDifference.toValue()
                                        )
                                    }
                                    .toSet(),
                                relatedLot = requestAuction.relatedLot.toString(),
                                electronicAuctionProgress = null,
                                electronicAuctionResult = null
                            )
                        } else
                            detail
                    }
                    .toSet()
            )
        }
    }

    private fun setUnsuccessfulStatusToLots(
        data: StartAwardPeriodAuctionData,
        record: Record
    ): List<Lot>? {
        val unsuccessfulLotsById = data.unsuccessfulLots.associateBy { it.id.toString() }

        return record.tender.lots
            ?.asSequence()
            ?.map { lot ->
                val id = lot.id
                val unsuccessfulLot = unsuccessfulLotsById[id]
                if (unsuccessfulLot != null)
                    lot.copy(status = unsuccessfulLot.status.value)
                else
                    lot
            }
            ?.toList()
    }

    fun auctionPeriodEnd(cpid: String, ocid: String, stage: String, releaseDate: LocalDateTime, data: JsonNode): ResponseDto {
        val dto = toObject(AuctionPeriodEndDto::class.java, data.toString())
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        val recordAwards = record.awards ?: setOf<Award>()
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tag = listOf(Tag.AWARD)
            tender.awardPeriod = dto.awardPeriod
            tender.statusDetails = dto.tenderStatusDetails
            tender.auctionPeriod = dto.tender.auctionPeriod
            tender.electronicAuctions = updateElectronicAuctions(dto = dto, record = record)
            if (dto.awards.isNotEmpty()) awards = recordAwards.plus(dto.awards).toHashSet()
            if (dto.bids.isNotEmpty() && dto.documents.isNotEmpty()) updateBidsDocuments(dto.bids, dto.documents)
            if (dto.bids.isNotEmpty()) bids = Bids(null, dto.bids)
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }


    fun suspendTender(cpid: String,
                      ocid: String,
                      stage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val dto = toObject(TenderStatusDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = dto.tenderStatusDetails
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun unsuspendTender(cpid: String,
                        ocid: String,
                        stage: String,
                        releaseDate: LocalDateTime,
                        data: JsonNode): ResponseDto {
        val dto = toObject(UnsuspendTenderDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpid, ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = dto.tender.statusDetails
            tender.tenderPeriod = dto.tender.tenderPeriod
            tender.enquiryPeriod = dto.tender.enquiryPeriod
            tender.auctionPeriod = dto.tender.auctionPeriod
            tender.procurementMethodModalities = dto.tender.procurementMethodModalities
            tender.electronicAuctions = dto.tender.electronicAuctions
        }
        addAnswerToEnquiry(record.tender.enquiries, dto.enquiry)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun tenderUnsuccessful(
        context: TenderUnsuccessfulContext,
        data: TenderUnsuccessfulData
    ): TenderUnsuccessfulResult {
        val msEntity = releaseService.getMsEntity(context.cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val updatedMS = ms.copy(
            id = releaseService.newReleaseId(context.cpid), //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.COMPILED), //FR-MR-5.7.2.2.1
            tender = ms.tender.copy(
                status = TenderStatus.fromValue(data.tender.status.value) //FR-MR-5.7.2.2.2
            )
        )

        val recordEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        val updatedLots = updateLots(data = data, lots = record.tender.lots ?: emptyList())
        val updatedBids = updateBids(data = data, bids = record.bids)
        val updatedAwards = updateAwards(data = data)
        val updatedParties = updateParties(data = data, parties = record.parties ?: emptyList())

        val updatedRecord = record.copy(
            id = releaseService.newReleaseId(context.ocid), //FR-5.0.1
            date = context.releaseDate, //FR-5.0.2
            tag = listOf(Tag.TENDER_CANCELLATION), //FR-ER-5.7.2.2.1

            //FR-ER-5.7.2.1.6
            tender = record.tender.copy(
                status = TenderStatus.fromValue(data.tender.status.value),
                statusDetails = TenderStatusDetails.fromValue(data.tender.statusDetails.value),
                lots = updatedLots.toHashSet()
            ),
            bids = updatedBids,//FR-ER-5.7.2.2.3
            awards = updatedAwards.toHashSet(), //FR-ER-5.7.2.2.4
            parties = updatedParties.toHashSet() //FR-ER-5.7.2.2.5
        )
        releaseService.saveMs(context.cpid, updatedMS, publishDate = msEntity.publishDate)
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            record = updatedRecord,
            publishDate = recordEntity.publishDate
        )
        return TenderUnsuccessfulResult(cpid = context.cpid, ocid = context.ocid)
    }

    private fun updateLots(data: TenderUnsuccessfulData, lots: Collection<Lot>): Collection<Lot> {
        val unsuccessfulLotsByIds = data.unsuccessfulLots.associateBy { it.id.toString() }
        return lots.map { lot ->
            val unsuccessfulLot = unsuccessfulLotsByIds[lot.id]
            if (unsuccessfulLot != null)
                lot.copy(
                    status = unsuccessfulLot.status.value
                )
            else
                lot
        }
    }

    private fun updateBids(data: TenderUnsuccessfulData, bids: Bids?): Bids? {
        val bidsDocumentsById = data.documents.associateBy { it.id }
        val details = data.bids
            .asSequence()
            .map { bid ->
                Bid(
                    id = bid.id,
                    date = bid.date,
                    status = bid.status.value,
                    statusDetails = bid.statusDetails.value,
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
                    relatedLots = bid.relatedLots
                        .map { it.toString() },
                    value = bid.value.toValue(),
                    documents = bid.documents
                        .asSequence()
                        .map { document ->
                            bidsDocumentsById.getValue(document.id)
                                .let {
                                    Document(
                                        documentType = it.documentType.value,
                                        id = it.id,
                                        title = it.title,
                                        description = it.description,
                                        relatedLots = it.relatedLots.map { relatedLot -> relatedLot.toString() },
                                        datePublished = it.datePublished,
                                        url = it.url,
                                        dateModified = null,
                                        format = null,
                                        language = null,
                                        relatedConfirmations = null
                                    )
                                }
                        }
                        .toHashSet(),
                    requirementResponses = bid.requirementResponses
                        .asSequence()
                        .map { requirementResponse ->
                            RequirementResponse(
                                id = requirementResponse.id,
                                title = requirementResponse.title,
                                description = requirementResponse.description,
                                value = requirementResponse.value,
                                requirement = RequirementReference(
                                    id = requirementResponse.requirement.id,
                                    title = null
                                ),
                                period = requirementResponse.period
                                    ?.let { period ->
                                        Period(
                                            startDate = period.startDate,
                                            endDate = period.endDate,
                                            durationInDays = null,
                                            maxExtentDate = null
                                        )
                                    },
                                relatedTenderer = null
                            )
                        }
                        .toHashSet()
                )
            }
            .toHashSet()
        return if(details.isNotEmpty())
            bids?.copy(details = details) ?: Bids(details = details, statistics = null)
        else
            null
    }

    private fun updateAwards(data: TenderUnsuccessfulData): List<Award> =
        data.awards
            .map { award ->
                Award(
                    id = award.id,
                    date = award.date,
                    status = award.status.value,
                    statusDetails = award.statusDetails.value,
                    title = award.title,
                    description = award.description,
                    relatedLots = award.relatedLots.map { it.toString() },
                    relatedBid = null,
                    value = null,
                    suppliers = null,
                    weightedValue = null,
                    amendment = null,
                    amendments = null,
                    items = null,
                    contractPeriod = null,
                    documents = null,
                    requirementResponses = null,
                    reviewProceedings = null
                )
            }

    private fun updateParties(
        data: TenderUnsuccessfulData,
        parties: Collection<Organization>
    ): Collection<Organization> {
        if (data.bids.isEmpty())
            return parties

        val tenderersById: Map<String, TenderUnsuccessfulData.Bid.Tenderer> = data.bids.asSequence()
            .flatMap {
                it.tenderers.asSequence()
            }
            .associateBy {
                it.id
            }

        val partiesById: Map<String, Organization> = parties.associateBy { it.id!! }

        val tenderersIds = tenderersById.keys
        val partiesIds = partiesById.keys
        val idsNewParties = getNewElements(tenderersIds, partiesIds)
        val idsUpdatingParties = getElementsForUpdate(tenderersIds, partiesIds)

        val updatedParties: Sequence<Organization> = parties.asSequence()
            .map { party ->
                val partyId = party.id!!
                if (partyId in idsUpdatingParties) {
                    if (PartyRole.TENDERER !in party.roles) {
                        val tenderer = tenderersById.getValue(partyId)
                        party.copy(
                            roles = (party.roles + PartyRole.TENDERER).toHashSet(),
                            persones = tenderer.persons
                                .mapIfNotEmpty { person ->
                                    Person(
                                        title = person.title,
                                        name = person.name,
                                        identifier = person.identifier
                                            .let { identifier ->
                                                Identifier(
                                                    scheme = identifier.scheme,
                                                    id = identifier.id,
                                                    uri = identifier.uri,
                                                    legalName = null
                                                )
                                            },
                                        businessFunctions = person.businessFunctions
                                            .map { businessFunction ->
                                                BusinessFunction(
                                                    id = businessFunction.id,
                                                    type = businessFunction.type.value,
                                                    jobTitle = businessFunction.jobTitle,
                                                    period = Period(
                                                        startDate = businessFunction.period.startDate,
                                                        endDate = null,
                                                        durationInDays = null,
                                                        maxExtentDate = null
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
                                }
                                ?.toHashSet()
                                ?: party.persones,
                            details = tenderer.details
                                .let { details ->
                                    Details(
                                        typeOfSupplier = details.typeOfSupplier?.value,
                                        mainEconomicActivities = details.mainEconomicActivities.toSet(),
                                        scale = details.scale.value,
                                        permits = details.permits
                                            .map { permit ->
                                                Permits(
                                                    scheme = permit.scheme,
                                                    id = permit.id,
                                                    url = permit.url,
                                                    permitDetails = permit.details.let { details ->
                                                        PermitDetails(
                                                            issuedBy = details.issuedBy.let { issuedBy ->
                                                                Issue(
                                                                    id = issuedBy.id,
                                                                    name = issuedBy.name
                                                                )
                                                            },
                                                            issuedThought = details.issuedThought.let { issuedThought ->
                                                                Issue(
                                                                    id = issuedThought.id,
                                                                    name = issuedThought.name
                                                                )
                                                            },
                                                            validityPeriod = details.validityPeriod.let { validityPeriod ->
                                                                Period(
                                                                    startDate = validityPeriod.startDate,
                                                                    endDate = validityPeriod.endDate,
                                                                    durationInDays = null,
                                                                    maxExtentDate = null
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            },
                                        bankAccounts = details.bankAccounts
                                            .map { bankAccount ->
                                                BankAccount(
                                                    description = bankAccount.description,
                                                    bankName = bankAccount.bankName,
                                                    address = bankAccount.address
                                                        .let { address ->
                                                            Address(
                                                                streetAddress = address.streetAddress,
                                                                postalCode = address.postalCode,
                                                                addressDetails = address.details.let { details ->
                                                                    AddressDetails(
                                                                        country = details.country.let { country ->
                                                                            CountryDetails(
                                                                                scheme = country.scheme,
                                                                                id = country.id,
                                                                                description = country.description,
                                                                                uri = country.uri
                                                                            )
                                                                        },
                                                                        region = details.region.let { region ->
                                                                            RegionDetails(
                                                                                scheme = region.scheme,
                                                                                id = region.id,
                                                                                description = region.description,
                                                                                uri = region.uri
                                                                            )
                                                                        },
                                                                        locality = details.locality.let { locality ->
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
                                        legalForm = details.legalForm
                                            ?.let { legalForm ->
                                                LegalForm(
                                                    scheme = legalForm.scheme,
                                                    id = legalForm.id,
                                                    description = legalForm.description,
                                                    uri = legalForm.uri
                                                )
                                            },
                                        isACentralPurchasingBody = null,
                                        mainGeneralActivity = null,
                                        mainSectoralActivity = null,
                                        typeOfBuyer = null,
                                        nutsCode = null
                                    )
                                }
                        )
                    } else
                        party
                } else
                    party
            }

        val newParties: Sequence<Organization> = idsNewParties.asSequence()
            .map { id ->
                val tenderer = tenderersById.getValue(id)
                Organization(
                    id = tenderer.id,
                    name = tenderer.name,
                    roles = hashSetOf(PartyRole.TENDERER),
                    identifier = tenderer.identifier
                        .let { identifier ->
                            Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                uri = identifier.uri,
                                legalName = null
                            )
                        },
                    additionalIdentifiers = tenderer.additionalIdentifiers
                        .asSequence()
                        .map { identifier ->
                            Identifier(
                                scheme = identifier.scheme,
                                id = identifier.id,
                                uri = identifier.uri,
                                legalName = null
                            )
                        }
                        .toHashSet(),
                    address = tenderer.address.let { address ->
                        Address(
                            streetAddress = address.streetAddress,
                            postalCode = address.postalCode,
                            addressDetails = address.details.let { details ->
                                AddressDetails(
                                    country = details.country.let { country ->
                                        CountryDetails(
                                            scheme = country.scheme,
                                            id = country.id,
                                            description = country.description,
                                            uri = country.uri
                                        )
                                    },
                                    region = details.region.let { region ->
                                        RegionDetails(
                                            scheme = region.scheme,
                                            id = region.id,
                                            description = region.description,
                                            uri = region.uri
                                        )
                                    },
                                    locality = details.locality.let { locality ->
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
                    contactPoint = tenderer.contactPoint.let { contactPoint ->
                        ContactPoint(
                            name = contactPoint.name,
                            email = contactPoint.email,
                            telephone = contactPoint.telephone,
                            faxNumber = contactPoint.faxNumber,
                            url = contactPoint.url
                        )
                    },
                    persones = tenderer.persons
                        .map { person ->
                            Person(
                                title = person.title,
                                name = person.name,
                                identifier = person.identifier
                                    .let { identifier ->
                                        Identifier(
                                            scheme = identifier.scheme,
                                            id = identifier.id,
                                            uri = identifier.uri,
                                            legalName = null
                                        )
                                    },
                                businessFunctions = person.businessFunctions
                                    .map { businessFunction ->
                                        BusinessFunction(
                                            id = businessFunction.id,
                                            type = businessFunction.type.value,
                                            jobTitle = businessFunction.jobTitle,
                                            period = Period(
                                                startDate = businessFunction.period.startDate,
                                                endDate = null,
                                                durationInDays = null,
                                                maxExtentDate = null
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
                        }
                        .toHashSet(),
                    details = tenderer.details.let { details ->
                        Details(
                            typeOfSupplier = details.typeOfSupplier?.value,
                            mainEconomicActivities = details.mainEconomicActivities.toSet(),
                            scale = details.scale.value,
                            permits = details.permits
                                .map { permit ->
                                    Permits(
                                        scheme = permit.scheme,
                                        id = permit.id,
                                        url = permit.url,
                                        permitDetails = permit.details.let { details ->
                                            PermitDetails(
                                                issuedBy = details.issuedBy.let { issuedBy ->
                                                    Issue(
                                                        id = issuedBy.id,
                                                        name = issuedBy.name
                                                    )
                                                },
                                                issuedThought = details.issuedThought.let { issuedThought ->
                                                    Issue(
                                                        id = issuedThought.id,
                                                        name = issuedThought.name
                                                    )
                                                },
                                                validityPeriod = details.validityPeriod.let { validityPeriod ->
                                                    Period(
                                                        startDate = validityPeriod.startDate,
                                                        endDate = validityPeriod.endDate,
                                                        durationInDays = null,
                                                        maxExtentDate = null
                                                    )
                                                }
                                            )
                                        }
                                    )
                                },
                            bankAccounts = details.bankAccounts
                                .map { bankAccount ->
                                    BankAccount(
                                        description = bankAccount.description,
                                        bankName = bankAccount.bankName,
                                        address = bankAccount.address
                                            .let { address ->
                                                Address(
                                                    streetAddress = address.streetAddress,
                                                    postalCode = address.postalCode,
                                                    addressDetails = address.details.let { details ->
                                                        AddressDetails(
                                                            country = details.country.let { country ->
                                                                CountryDetails(
                                                                    scheme = country.scheme,
                                                                    id = country.id,
                                                                    description = country.description,
                                                                    uri = country.uri
                                                                )
                                                            },
                                                            region = details.region.let { region ->
                                                                RegionDetails(
                                                                    scheme = region.scheme,
                                                                    id = region.id,
                                                                    description = region.description,
                                                                    uri = region.uri
                                                                )
                                                            },
                                                            locality = details.locality.let { locality ->
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
                            legalForm = details.legalForm
                                ?.let { legalForm ->
                                    LegalForm(
                                        scheme = legalForm.scheme,
                                        id = legalForm.id,
                                        description = legalForm.description,
                                        uri = legalForm.uri
                                    )
                                },
                            isACentralPurchasingBody = null,
                            mainGeneralActivity = null,
                            mainSectoralActivity = null,
                            typeOfBuyer = null,
                            nutsCode = null
                        )
                    },
                    buyerProfile = null
                )
            }

        return (updatedParties + newParties).toList()
    }

    private fun <T> getNewElements(received: Set<T>, saved: Set<T>) = received.subtract(saved)

    private fun <T> getElementsForUpdate(received: Set<T>, saved: Set<T>) = saved.intersect(received)

    fun awardByBid(cpid: String,
                   ocid: String,
                   stage: String,
                   releaseDate: LocalDateTime,
                   data: JsonNode): ResponseDto {
        val dto = toObject(AwardByBidDto::class.java, toJson(data))
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            tag = listOf(Tag.AWARD_UPDATE)
            date = releaseDate
            updateAward(this, dto.award)
            updateBid(this, dto.bid)
        }
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun awardPeriodEnd(cpid: String,
                       ocid: String,
                       stage: String,
                       releaseDate: LocalDateTime,
                       data: JsonNode): ResponseDto {
        val dto = toObject(AwardPeriodEndDto::class.java, data.toString())
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = TenderStatusDetails.COMPLETE
            tender.awardPeriod = dto.awardPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
            if (dto.awards.isNotEmpty()) awards?.let { updateAwards(it, dto.awards) }
            if (dto.bids.isNotEmpty()) bids?.details?.let { updateBids(it, dto.bids) }
        }
        organizationService.processRecordPartiesFromBids(record)
        organizationService.processRecordPartiesFromAwards(record)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun standstillPeriod(cpid: String,
                         ocid: String,
                         stage: String,
                         releaseDate: LocalDateTime,
                         data: JsonNode): ResponseDto {
        val dto = toObject(StandstillPeriodEndDto::class.java, toJson(data))
        val statusDetails = when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PS -> TenderStatusDetails.PRESELECTED
            Stage.PQ -> TenderStatusDetails.PREQUALIFIED
            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)
        record.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.statusDetails = statusDetails
            tender.standstillPeriod = dto.standstillPeriod
            if (dto.lots.isNotEmpty()) tender.lots = dto.lots
        }
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = recordEntity.publishDate)
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    fun startNewStage(cpid: String,
                      ocid: String,
                      stage: String,
                      prevStage: String,
                      releaseDate: LocalDateTime,
                      data: JsonNode): ResponseDto {
        val dto = toObject(StartNewStageDto::class.java, toJson(data))
        val statusDetails: TenderStatusDetails?
        val relatedProcessType: RelatedProcessType?
        when (Stage.valueOf(stage.toUpperCase())) {
            Stage.PQ -> {
                statusDetails = TenderStatusDetails.PREQUALIFICATION
                relatedProcessType = RelatedProcessType.X_PREQUALIFICATION
            }
            Stage.EV -> {
                statusDetails = TenderStatusDetails.EVALUATION
                relatedProcessType = RelatedProcessType.X_EVALUATION
            }
            else -> throw ErrorException(ErrorType.STAGE_ERROR)
        }
        val prRelatedProcessType = when (Stage.valueOf(prevStage.toUpperCase())) {
            Stage.PS -> RelatedProcessType.X_PRESELECTION
            Stage.PQ -> RelatedProcessType.X_PREQUALIFICATION
            else -> throw ErrorException(ErrorType.IMPLEMENTATION_ERROR)
        }
        val msEntity = releaseService.getMsEntity(cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        ms.apply {
            id = releaseService.newReleaseId(cpid)
            date = releaseDate
            tag = listOf(Tag.COMPILED)
            tender.statusDetails = statusDetails
        }
        /*prev record*/
        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val prevRecord = releaseService.getRecord(recordEntity.jsonData)
        prevRecord.apply {
            id = releaseService.newReleaseId(ocid)
            date = releaseDate
            tender.status = TenderStatus.COMPLETE
            tender.statusDetails = TenderStatusDetails.EMPTY
        }
        /*new record*/
        val newOcId = releaseService.newOcId(cpId = cpid, stage = stage)
        dto.tender.title = TenderTitle.valueOf(stage.toUpperCase()).text
        dto.tender.description = TenderDescription.valueOf(stage.toUpperCase()).text
        val record = Record(
                ocid = newOcId,
                id = releaseService.newReleaseId(newOcId),
                date = releaseDate,
                tag = listOf(Tag.COMPILED),
                initiationType = InitiationType.TENDER,
                parties = null,
                tender = dto.tender,
                awards = null,
                bids = dto.bids,
                contracts = null,
                hasPreviousNotice = prevRecord.hasPreviousNotice,
                purposeOfNotice = prevRecord.purposeOfNotice,
                relatedProcesses = null)
        processTenderDocuments(record = record, prevRecord = prevRecord)
        organizationService.processRecordPartiesFromBids(record)
        relatedProcessService.addRecordRelatedProcessToMs(ms = ms, ocid = newOcId, processType = relatedProcessType)
        relatedProcessService.addMsRelatedProcessToRecord(record = record, cpId = cpid)
        relatedProcessService.addRecordRelatedProcessToRecord(record = record, ocId = ocid, cpId = cpid, processType = prRelatedProcessType)
        releaseService.saveMs(cpId = cpid, ms = ms, publishDate = msEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = prevStage, record = prevRecord, publishDate = recordEntity.publishDate)
        releaseService.saveRecord(cpId = cpid, stage = stage, record = record, publishDate = releaseDate.toDate())
        return ResponseDto(data = DataResponseDto(cpid = cpid, ocid = ocid))
    }

    private fun processTenderDocuments(record: Record, prevRecord: Record) {
        prevRecord.tender.documents?.let { updateTenderDocuments(record.tender, it) }
    }

    private fun updateAwards(recordAwards: HashSet<Award>, dtoAwards: HashSet<Award>) {
        for (award in recordAwards) {
            dtoAwards.firstOrNull { it.id == award.id }?.apply {
                award.date = this.date
                award.status = this.status
                award.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateBids(recordBids: HashSet<Bid>, dtoBids: HashSet<Bid>) {
        for (bid in recordBids) {
            dtoBids.firstOrNull { it.id == bid.id }?.apply {
                bid.date = this.date
                bid.status = this.status
                bid.statusDetails = this.statusDetails
            }
        }
    }

    private fun updateTenderDocuments(tender: RecordTender, documents: HashSet<Document>) {
        tender.documents?.let { tenderDocuments ->
            documents.forEach { document ->
                tenderDocuments.firstOrNull { it.id == document.id }?.apply {
                    datePublished = document.datePublished
                    url = document.url
                }
            }
        }
    }

    private fun updateElectronicAuctions(dto: AuctionPeriodEndDto, record: Record): ElectronicAuctions? {
        val electronicAuctions: ElectronicAuctions = record.tender.electronicAuctions
            ?.takeIf { it.details.isNotEmpty() }
            ?: return record.tender.electronicAuctions

        val requestElectronicAuctionsDetailsByIds: Map<String, ElectronicAuctionsDetails> =
            dto.tender.electronicAuctions.details.associateBy { it.id!! }

        return ElectronicAuctions(
            details = electronicAuctions.details
                .asSequence()
                .map { detail ->
                    requestElectronicAuctionsDetailsByIds[detail.id!!] ?: detail
                }
                .toSet()
        )
    }

    private fun updateBidsDocuments(bids: HashSet<Bid>, documents: HashSet<Document>) {
        bids.forEach { bid ->
            bid.documents?.let { bidDocuments ->
                documents.forEach { document ->
                    bidDocuments.forEach { bidDocument ->
                        if (bidDocument.id == document.id) {
                            bidDocument.datePublished = document.datePublished
                            bidDocument.url = document.url
                        }
                    }
                }
            }
        }
    }

    private fun updateAward(record: Record, award: Award) {
        record.awards?.let { awards ->
            val upAward = awards.asSequence().firstOrNull { it.id == award.id }
                    ?: throw ErrorException(ErrorType.AWARD_NOT_FOUND)
            award.date?.let { upAward.date = it }
            award.description?.let { upAward.description = it }
            award.statusDetails?.let { upAward.statusDetails = it }
            award.documents?.let { upAward.documents = it }
        }
    }

    private fun updateBid(record: Record, bid: Bid) {
        record.bids?.details?.let { bids ->
            val upBid = bids.asSequence().firstOrNull { it.id == bid.id }
                    ?: throw ErrorException(ErrorType.BID_NOT_FOUND)
            bid.date?.let { upBid.date = it }
            bid.statusDetails?.let { upBid.statusDetails = it }
        }
    }

    private fun addAnswerToEnquiry(enquiries: HashSet<RecordEnquiry>?, enquiry: RecordEnquiry) {
        enquiries?.asSequence()?.firstOrNull { it.id == enquiry.id }?.apply {
            this.answer = enquiry.answer
            this.dateAnswered = enquiry.dateAnswered
        } ?: throw ErrorException(ErrorType.ENQUIRY_NOT_FOUND)
    }

}