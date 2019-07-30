package com.procurement.notice.application.service.award

import com.procurement.notice.model.ocds.Address
import com.procurement.notice.model.ocds.AddressDetails
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.ContactPoint
import com.procurement.notice.model.ocds.CountryDetails
import com.procurement.notice.model.ocds.Details
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Identifier
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Value
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toDate
import org.springframework.stereotype.Service

interface AwardService {
    fun createAward(context: CreateAwardContext, data: CreateAwardData)

    fun startAwardPeriod(context: StartAwardPeriodContext, data: StartAwardPeriodData)

    fun evaluate(context: EvaluateAwardContext, data: EvaluateAwardData)
}

@Service
class AwardServiceImpl(
    private val releaseService: ReleaseService
) : AwardService {
    override fun createAward(context: CreateAwardContext, data: CreateAwardData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val stage = Stage.valueOf(context.stage.toUpperCase())
        val releaseDate = context.releaseDate

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        val tender = record.tender

        //BR-2.6.18.8 + BR-2.6.18.9
        val newAward = convertAward(data.award)
        val updatedAwards = record.awards?.plus(newAward) ?: setOf(newAward)

        //BR-2.6.18.10
        val updatedParties = updateParties(
            parties = record.parties ?: emptyList(),
            suppliers = data.award.suppliers
        )

        val newRecord = record.copy(
            //BR-2.6.18.4
            id = releaseService.newReleaseId(ocid),

            //BR-2.6.18.1
            tag = listOf(Tag.AWARD),

            //BR-2.6.18.3
            date = releaseDate,

            //BR-2.6.18.8
            awards = updatedAwards.toHashSet(),

            //BR-2.6.18.10
            parties = updatedParties.toHashSet()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage.name,
            record = newRecord,
            publishDate = releaseDate.toDate()
        )
    }

    /**
     * BR-2.6.18.8 Awards
     *
     * eNotice executes next operations:
     * Saves in new Release Award object from Request as a awards object;
     * Changes the Supplier (awards.supplier) object in saved object by rule BR-2.6.18.9;
     */
    private fun convertAward(award: CreateAwardData.Award): Award = Award(
        id = award.id,
        title = null,
        description = award.description,
        status = award.status,
        statusDetails = award.statusDetails,
        date = award.date,
        value = award.value.let { value ->
            Value(
                amount = value.amount,
                currency = value.currency,
                amountNet = null,
                valueAddedTaxIncluded = null
            )
        },
        suppliers = award.suppliers.map { supplier ->
            convertSupplier(id = supplier.id, name = supplier.name)
        },
        relatedLots = award.relatedLots.toList(),
        items = null,
        contractPeriod = null,
        documents = null,
        amendments = null,
        amendment = null,
        requirementResponses = null,
        reviewProceedings = null,
        relatedBid = null
    )

    /**
     * BR-2.6.18.9 Supplier
     *
     * eNotice in Awards object of Release saves only next fields from Request:
     * Supplier.ID
     * Supplier.Name
     */
    private fun convertSupplier(id: String, name: String): OrganizationReference =
        OrganizationReference(
            id = id,
            name = name,
            identifier = null,
            address = null,
            additionalIdentifiers = null,
            contactPoint = null,
            details = null,
            buyerProfile = null,
            persones = null
        )

    /**
     * BR-2.6.18.10 Parties
     *
     * eNotice executes next steps:
     * 1. Gets Parties object from actual Release and writes it to new formed Release;
     * 2. Checks the availability of organization in Parties object with organization.ID == award.supplier.ID
     *    from Award object of Request:
     *      a. IF there is NO such organization, eNotice executes next operation:
     *        i.  Saves supplier object from Request as a new Parties object;
     *        ii. Sets for added object parties.role == "supplier";
     *      b. ELSE
     *         (parties object with same ID was found), eNotice checks the availability parties.role value == "supplier":
     *        i.  IF there is NO "supplier" role,
     *            eNotice adds new parties.role == "supplier" for Parties object;
     *        ii. ELSE
     *            "supplier" role is present, eNotice does not perform any operation;
     */
    private fun updateParties(
        parties: Collection<Organization>,
        suppliers: List<CreateAwardData.Award.Supplier>
    ): Collection<Organization> {
        val suppliersById = suppliers.associateBy { it.id }
        val partiesById = parties.associateBy { it.id!! }
        val ids = partiesById.keys.union(suppliersById.keys)
        return ids.map { id ->
            partiesById[id]?.let { party ->
                updatePartyRoles(party)
            } ?: convertSupplier(suppliersById.getValue(id))
        }
    }

    private fun updatePartyRoles(party: Organization): Organization =
        if (PartyRole.SUPPLIER in party.roles)
            party
        else
            party.copy(roles = party.roles.plus(PartyRole.SUPPLIER).toHashSet())

    private fun convertSupplier(supplier: CreateAwardData.Award.Supplier): Organization = Organization(
        id = supplier.id,
        name = supplier.name,
        identifier = supplier.identifier.let { identifier ->
            Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            )
        },
        additionalIdentifiers = supplier.additionalIdentifiers?.map { additionalIdentifier ->
            Identifier(
                scheme = additionalIdentifier.scheme,
                id = additionalIdentifier.id,
                legalName = additionalIdentifier.legalName,
                uri = additionalIdentifier.uri
            )
        }?.toHashSet(),
        address = supplier.address.let { address ->
            Address(
                streetAddress = address.streetAddress,
                postalCode = address.postalCode,
                addressDetails = address.addressDetails.let { addressDetails ->
                    AddressDetails(
                        country = addressDetails.country.let { country ->
                            CountryDetails(
                                scheme = country.scheme,
                                id = country.id,
                                description = country.description,
                                uri = country.uri
                            )
                        },
                        region = addressDetails.region.let { region ->
                            RegionDetails(
                                scheme = region.scheme,
                                id = region.id,
                                description = region.description,
                                uri = region.uri
                            )
                        },
                        locality = addressDetails.locality.let { locality ->
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
        contactPoint = supplier.contactPoint.let { contactPoint ->
            ContactPoint(
                name = contactPoint.name,
                email = contactPoint.email,
                telephone = contactPoint.telephone,
                faxNumber = contactPoint.faxNumber,
                url = contactPoint.url
            )
        },
        details = Details(
            scale = supplier.details.scale,
            typeOfBuyer = null,
            typeOfSupplier = null,
            mainEconomicActivities = null,
            mainGeneralActivity = null,
            mainSectoralActivity = null,
            permits = null,
            bankAccounts = null,
            legalForm = null,
            isACentralPurchasingBody = null,
            nutsCode = null
        ),
        buyerProfile = null,
        persones = null,
        roles = hashSetOf(PartyRole.SUPPLIER)
    )

    override fun startAwardPeriod(context: StartAwardPeriodContext, data: StartAwardPeriodData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val stage = Stage.valueOf(context.stage.toUpperCase())
        val releaseDate = context.releaseDate

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        val tender = record.tender

        //BR-2.6.18.8 + BR-2.6.18.9
        val newAward = convertAward(data.award)
        val updatedAwards = record.awards?.plus(newAward) ?: setOf(newAward)

        //BR-2.6.18.10
        val updatedParties = updateParties2(
            parties = record.parties ?: emptyList(),
            suppliers = data.award.suppliers
        )

        val newRecord = record.copy(
            //BR-2.6.20.4
            id = releaseService.newReleaseId(ocid),

            //BR-2.6.20.1
            tag = listOf(Tag.AWARD),

            //BR-2.6.18.3
            date = releaseDate,

            //BR-2.6.20.6
            tender = tender.copy(
                //BR-2.6.20.7
                statusDetails = data.tender.statusDetails,

                //BR-2.6.20.8
                awardPeriod = tender.awardPeriod?.copy(
                    startDate = data.awardPeriod.startDate
                ) ?: Period(
                    startDate = data.awardPeriod.startDate,
                    endDate = null,
                    maxExtentDate = null,
                    durationInDays = null
                )
            ),

            //BR-2.6.18.8
            awards = updatedAwards.toHashSet(),

            //BR-2.6.18.10
            parties = updatedParties.toHashSet()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage.name,
            record = newRecord,
            publishDate = releaseDate.toDate()
        )
    }

    /**
     * BR-2.6.20.9 Awards
     *
     * eNotice executes next operations:
     * Saves in new Release Award object from Request as a awards object;
     * Changes the Supplier (awards.supplier) object in saved object by rule BR-2.6.18.9;
     */
    private fun convertAward(award: StartAwardPeriodData.Award): Award = Award(
        id = award.id,
        title = null,
        description = award.description,
        status = award.status,
        statusDetails = award.statusDetails,
        date = award.date,
        value = award.value.let { value ->
            Value(
                amount = value.amount,
                currency = value.currency,
                amountNet = null,
                valueAddedTaxIncluded = null
            )
        },
        suppliers = award.suppliers.map { supplier ->
            convertSupplierForStartAwardPeriod(id = supplier.id, name = supplier.name)
        },
        relatedLots = award.relatedLots.toList(),
        items = null,
        contractPeriod = null,
        documents = null,
        amendments = null,
        amendment = null,
        requirementResponses = null,
        reviewProceedings = null,
        relatedBid = null
    )

    /**
     * BR-2.6.20.10 Supplier
     *
     * eNotice in Awards object of Release saves only next fields from Request:
     * Supplier.ID
     * Supplier.Name
     */
    private fun convertSupplierForStartAwardPeriod(id: String, name: String): OrganizationReference =
        OrganizationReference(
            id = id,
            name = name,
            identifier = null,
            address = null,
            additionalIdentifiers = null,
            contactPoint = null,
            details = null,
            buyerProfile = null,
            persones = null
        )

    /**
     * BR-2.6.20.11 Parties
     *
     * eNotice executes next steps:
     * 1. Gets Parties object from actual Release and writes it to new formed Release;
     * 2. Checks the availability of organization in Parties object with organization.ID == award.supplier.ID
     *    from Award object of Request:
     *   a. IF there is NO such organization, eNotice executes next operation:
     *     i.  Saves supplier object from Request as a new Parties object;
     *     ii. Sets for added object parties.role == "supplier";
     *   b. ELSE (parties object with same ID was found)
     *        eNotice checks the availability parties.role value == "supplier":
     *     i.  IF there is NO "supplier" role
     *           eNotice adds new parties.role == "supplier" for Parties object;
     *     ii. ELSE "supplier" role is present
     *           eNotice does not perform any operation;
     */
    private fun updateParties2(
        parties: Collection<Organization>,
        suppliers: List<StartAwardPeriodData.Award.Supplier>
    ): Collection<Organization> {
        val suppliersById = suppliers.associateBy { it.id }
        val partiesById = parties.associateBy { it.id!! }
        val ids = partiesById.keys.union(suppliersById.keys)
        return ids.map { id ->
            partiesById[id]?.let { party ->
                updatePartyRoles(party)
            } ?: convertSupplier(suppliersById.getValue(id))
        }
    }

    private fun convertSupplier(supplier: StartAwardPeriodData.Award.Supplier): Organization = Organization(
        id = supplier.id,
        name = supplier.name,
        identifier = supplier.identifier.let { identifier ->
            Identifier(
                scheme = identifier.scheme,
                id = identifier.id,
                legalName = identifier.legalName,
                uri = identifier.uri
            )
        },
        additionalIdentifiers = supplier.additionalIdentifiers?.map { additionalIdentifier ->
            Identifier(
                scheme = additionalIdentifier.scheme,
                id = additionalIdentifier.id,
                legalName = additionalIdentifier.legalName,
                uri = additionalIdentifier.uri
            )
        }?.toHashSet(),
        address = supplier.address.let { address ->
            Address(
                streetAddress = address.streetAddress,
                postalCode = address.postalCode,
                addressDetails = address.addressDetails.let { addressDetails ->
                    AddressDetails(
                        country = addressDetails.country.let { country ->
                            CountryDetails(
                                scheme = country.scheme,
                                id = country.id,
                                description = country.description,
                                uri = country.uri
                            )
                        },
                        region = addressDetails.region.let { region ->
                            RegionDetails(
                                scheme = region.scheme,
                                id = region.id,
                                description = region.description,
                                uri = region.uri
                            )
                        },
                        locality = addressDetails.locality.let { locality ->
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
        contactPoint = supplier.contactPoint.let { contactPoint ->
            ContactPoint(
                name = contactPoint.name,
                email = contactPoint.email,
                telephone = contactPoint.telephone,
                faxNumber = contactPoint.faxNumber,
                url = contactPoint.url
            )
        },
        details = Details(
            scale = supplier.details.scale,
            typeOfBuyer = null,
            typeOfSupplier = null,
            mainEconomicActivities = null,
            mainGeneralActivity = null,
            mainSectoralActivity = null,
            permits = null,
            bankAccounts = null,
            legalForm = null,
            isACentralPurchasingBody = null,
            nutsCode = null
        ),
        buyerProfile = null,
        persones = null,
        roles = hashSetOf(PartyRole.SUPPLIER)
    )

    override fun evaluate(context: EvaluateAwardContext, data: EvaluateAwardData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val stage = Stage.valueOf(context.stage.toUpperCase())
        val releaseDate = context.releaseDate

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val record = releaseService.getRecord(recordEntity.jsonData)

        //BR-2.6.21.7
        val awards: Set<Award>? = record.awards
        val updatedAwards = if (awards != null && awards.isNotEmpty()) {
            awards.asSequence()
                .map { award ->
                    if (data.award.id == award.id) {
                        convertAward(data.award)
                    } else
                        award
                }
                .toList()
        } else {
            listOf(convertAward(data.award))
        }

        val newRecord = record.copy(
            //BR-2.6.21.4
            id = releaseService.newReleaseId(ocid),

            //BR-2.6.21.1
            tag = listOf(Tag.AWARD_UPDATE),

            //BR-2.6.21.3
            date = releaseDate,

            //BR-2.6.21.7
            awards = updatedAwards.toHashSet()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage.name,
            record = newRecord,
            publishDate = releaseDate.toDate()
        )
    }

    private fun convertAward(award: EvaluateAwardData.Award): Award = Award(
        id = award.id,
        date = award.date,
        title = null,
        description = award.description,
        status = award.status,
        statusDetails = award.statusDetails,

        value = award.value.let { value ->
            Value(
                amount = value.amount,
                currency = value.currency,
                amountNet = null,
                valueAddedTaxIncluded = null
            )
        },
        suppliers = award.suppliers.map { supplier ->
            convertSupplier(id = supplier.id, name = supplier.name)
        },
        relatedLots = award.relatedLots.map { it.toString() },
        items = null,
        contractPeriod = null,
        documents = award.documents?.map { document ->
            Document(
                documentType = document.documentType,
                id = document.id,
                datePublished = document.datePublished,
                url = document.url,
                title = document.title,
                description = document.description,
                relatedLots = document.relatedLots.map { it.toString() },
                dateModified = null,
                format = null,
                language = null,
                relatedConfirmations = null
            )
        },
        amendments = null,
        amendment = null,
        requirementResponses = null,
        reviewProceedings = null,
        relatedBid = null
    )
}
