package com.procurement.notice.application.service.award

import com.procurement.notice.application.service.GenerationService
import com.procurement.notice.application.service.award.auction.AwardConsiderationContext
import com.procurement.notice.dao.ReleaseDao
import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.domain.model.award.AwardId
import com.procurement.notice.domain.model.bid.BidId
import com.procurement.notice.domain.model.enums.TenderStatusDetails
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.ocds.Address
import com.procurement.notice.model.ocds.AddressDetails
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.ocds.ContactPoint
import com.procurement.notice.model.ocds.Contract
import com.procurement.notice.model.ocds.CountryDetails
import com.procurement.notice.model.ocds.Details
import com.procurement.notice.model.ocds.Document
import com.procurement.notice.model.ocds.Identifier
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Milestone
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.RelatedParty
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Value
import com.procurement.notice.model.ocds.toValue
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toDate
import com.procurement.notice.utils.toObject
import org.springframework.stereotype.Service

interface AwardService {
    fun createAward(context: CreateAwardContext, data: CreateAwardData)

    fun startAwardPeriod(context: StartAwardPeriodContext, data: StartAwardPeriodData)

    fun endAwardPeriod(context: EndAwardPeriodContext, data: EndAwardPeriodData)

    fun evaluate(context: EvaluateAwardContext, data: EvaluateAwardData)

    fun consider(context: AwardConsiderationContext, data: AwardConsiderationData)
}

@Service
class AwardServiceImpl(
    private val releaseService: ReleaseService,
    private val releaseDao: ReleaseDao,
    private val generationService: GenerationService
) : AwardService {
    override fun createAward(context: CreateAwardContext, data: CreateAwardData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val stage = Stage.valueOf(context.stage.toUpperCase())
        val releaseDate = context.releaseDate

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        val tender = release.tender

        //BR-2.6.18.8 + BR-2.6.18.9
        val newAward = convertAward(data.award)
        val updatedAwards = release.awards.plus(newAward)

        //BR-2.6.18.10
        val updatedParties = updateParties(
            parties = release.parties,
            suppliers = data.award.suppliers
        )

        val newRecord = release.copy(
            //BR-2.6.18.4
            id = generationService.generateReleaseId(ocid = ocid),

            //BR-2.6.18.1
            tag = listOf(Tag.AWARD),

            //BR-2.6.18.3
            date = releaseDate,

            //BR-2.6.18.8
            awards = updatedAwards.toList(),

            //BR-2.6.18.10
            parties = updatedParties.toMutableList()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage.name,
            release = newRecord,
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
            party.copy(roles = party.roles.plus(PartyRole.SUPPLIER).toMutableList())

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
        },
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
        roles = mutableListOf(PartyRole.SUPPLIER)
    )

    override fun startAwardPeriod(context: StartAwardPeriodContext, data: StartAwardPeriodData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val stage = Stage.valueOf(context.stage.toUpperCase())
        val releaseDate = context.releaseDate

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        val tender = release.tender

        //BR-2.6.18.8 + BR-2.6.18.9
        val newAward = convertAward(data.award)
        val updatedAwards = release.awards.plus(newAward)

        //BR-2.6.18.10
        val updatedParties = updateParties2(
            parties = release.parties,
            suppliers = data.award.suppliers
        )

        val newRecord = release.copy(
            //BR-2.6.20.4
            id = generationService.generateReleaseId(ocid = ocid),

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
            awards = updatedAwards.toList(),

            //BR-2.6.18.10
            parties = updatedParties.toMutableList()
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage.name,
            release = newRecord,
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
        },
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
        roles = mutableListOf(PartyRole.SUPPLIER)
    )

    override fun evaluate(context: EvaluateAwardContext, data: EvaluateAwardData) {
        val cpid = context.cpid
        val ocid = context.ocid
        val stage = Stage.valueOf(context.stage.toUpperCase())
        val releaseDate = context.releaseDate

        val recordEntity = releaseService.getRecordEntity(cpId = cpid, ocId = ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        //FR-5.7.1.1.3 1)-3)
        val awards: List<Award> = release.awards
            .takeIf { awards ->
                awards.isNotEmpty()
            }
            ?: throw ErrorException(
                error = ErrorType.AWARD_NOT_FOUND,
                message = "No awards in the database found."
            )

        awards.checkAwardAvailability(requestAwardId = data.award.id)

        val requestAwardIdAsString = data.award.id.toString()
        val updatedAwards = awards.asSequence()
            .map { award ->
                if (award.id == requestAwardIdAsString) {
                    data.award.convertBy(award)
                } else if (data.nextAwardForUpdate != null && award.id == data.nextAwardForUpdate.id.toString()) {
                    //FR-5.7.1.1.3 4
                    award.copy(statusDetails = data.nextAwardForUpdate.statusDetails.value)
                } else
                    award
            }
            .toList()

        val requestBid = data.bid
        //FR-5.7.1.1.4
        val updatedBids = if (requestBid != null) {
            val requestBidId = requestBid.id.toString()
            release.bids
                ?.let { bids ->
                    bids.copy(
                        details = bids.details
                            ?.map { bid ->
                                if (bid.id == requestBidId) {
                                    bid.copy(
                                        documents = requestBid.documents
                                            .map { document ->
                                                Document(
                                                    id = document.id,
                                                    url = document.url,
                                                    title = document.title,
                                                    relatedLots = document.relatedLots.map { it.toString() },
                                                    documentType = document.documentType.value,
                                                    description = document.description,
                                                    datePublished = document.datePublished,
                                                    dateModified = null,
                                                    format = null,
                                                    language = null,
                                                    relatedConfirmations = null
                                                )
                                            }
                                    )
                                } else
                                    bid
                            }
                    )
                }
        } else
            release.bids

        val newRecord = release.copy(
            //FR-5.0.1
            id = generationService.generateReleaseId(ocid = ocid),

            //FR-5.7.1.1.1
            tag = listOf(Tag.AWARD_UPDATE),

            //FR-5.0.2
            date = releaseDate,

            //FR-5.7.1.1.3 4)
            awards = updatedAwards.toList(),

            //FR-5.7.1.1.4
            bids = updatedBids
        )

        releaseService.saveRecord(
            cpId = cpid,
            stage = stage.name,
            release = newRecord,
            publishDate = recordEntity.publishDate
        )
    }

    private fun Collection<Award>.checkAwardAvailability(requestAwardId: AwardId) {
        val requestAwardIdAsString = requestAwardId.toString()
        if (this.none { award -> award.id == requestAwardIdAsString }) {
            throw ErrorException(
                error = ErrorType.AWARD_NOT_FOUND,
                message = "The award '$requestAwardId' from request is not found in the database."
            )
        }
    }

    private fun EvaluateAwardData.Award.convertBy(original: Award): Award = Award(
        id = id.toString(),
        internalId = internalId,
        date = date,
        title = null,
        description = description,
        status = status.value,
        statusDetails = statusDetails.value,

        value = value.let { value ->
            Value(
                amount = value.amount,
                currency = value.currency,
                amountNet = null,
                valueAddedTaxIncluded = null
            )
        },
        suppliers = suppliers
            .map { supplier ->
                convertSupplier(id = supplier.id, name = supplier.name)
            },
        relatedLots = relatedLots.map { it.toString() },
        items = null,
        contractPeriod = null,
        documents = documents.map { document ->
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
        requirementResponses = original.requirementResponses,
        reviewProceedings = null,
        relatedBid = relatedBid?.toString(),
        weightedValue = weightedValue?.toValue()
    )

    override fun endAwardPeriod(context: EndAwardPeriodContext, data: EndAwardPeriodData) {
        val msEntity = releaseService.getMsEntity(cpid = context.cpid)
        val ms = releaseService.getMs(msEntity.jsonData)
        val updatedMS = ms.copy(
            id = generationService.generateReleaseId(ocid = context.cpid),
            date = context.releaseDate,
            tag = listOf(Tag.COMPILED),
            tender = ms.tender.copy(
                statusDetails = TenderStatusDetails.EXECUTION
            )
        )

        val recordStage = when (context.pmd) {
            ProcurementMethod.MV, ProcurementMethod.TEST_MV,
            ProcurementMethod.OT, ProcurementMethod.TEST_OT,
            ProcurementMethod.SV, ProcurementMethod.TEST_SV -> "EV"

            ProcurementMethod.CD, ProcurementMethod.TEST_CD,
            ProcurementMethod.DA, ProcurementMethod.TEST_DA,
            ProcurementMethod.DC, ProcurementMethod.TEST_DC,
            ProcurementMethod.IP, ProcurementMethod.TEST_IP,
            ProcurementMethod.NP, ProcurementMethod.TEST_NP,
            ProcurementMethod.OP, ProcurementMethod.TEST_OP -> "NP"

            ProcurementMethod.RFQ, ProcurementMethod.TEST_RFQ -> "RQ"

            ProcurementMethod.GPA, ProcurementMethod.TEST_GPA,
            ProcurementMethod.RT, ProcurementMethod.TEST_RT -> "TP"

            ProcurementMethod.CF, ProcurementMethod.TEST_CF,
            ProcurementMethod.OF, ProcurementMethod.TEST_OF,
            ProcurementMethod.FA, ProcurementMethod.TEST_FA -> throw ErrorException(ErrorType.INVALID_PMD)
        }
        val recordEvEntity = releaseDao.getByCpIdAndStage(cpId = context.cpid, stage = recordStage)
            ?: throw ErrorException(ErrorType.RECORD_NOT_FOUND)
        val releaseEV = releaseService.getRelease(recordEvEntity.jsonData)

        val updatedRecordEV = releaseEV.let { record ->
            record.copy(
                id = generationService.generateReleaseId(ocid = recordEvEntity.ocId),
                date = context.releaseDate,
                tag = listOf(Tag.TENDER_UPDATE),
                tender = record.tender.let { tender ->
                    tender.copy(
                        awardPeriod = data.awardPeriod.let { awardPeriod ->
                            Period(
                                startDate = awardPeriod.startDate,
                                endDate = awardPeriod.endDate,
                                durationInDays = null,
                                maxExtentDate = null
                            )
                        },
                        status = data.tender.status,
                        statusDetails = data.tender.statusDetails,
                        lots = updateLots(data, tender.lots).toList()
                    )
                },
                bids = updateBids(data, record.bids),
                awards = updateAwards(data, record.awards).toList(),
                contracts = updateCanContracts(data, record.contracts).toList()
            )
        }

        val (updatedContractRecord, contractPublishDate) = data.contract
            ?.let { contract ->
                val contractRecordEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
                val contractRecord = toObject(ContractRecord::class.java, contractRecordEntity.jsonData)

                contractRecord.copy(
                    id = generationService.generateReleaseId(ocid = context.ocid),
                    tag = listOf(Tag.CONTRACT_UPDATE),
                    date = context.releaseDate,
                    contracts = updateContracts(contract, contractRecord.contracts!!)
                ) to contractRecordEntity.publishDate
            }
            ?: Pair(null, null)

        releaseService.saveMs(
            cpId = context.cpid,
            ms = updatedMS,
            publishDate = msEntity.publishDate
        )
        releaseService.saveRecord(
            cpId = context.cpid,
            stage = recordStage,
            release = updatedRecordEV,
            publishDate = recordEvEntity.publishDate
        )
        if (updatedContractRecord != null)
            releaseService.saveContractRecord(
                cpId = context.cpid,
                stage = context.stage,
                record = updatedContractRecord,
                publishDate = contractPublishDate!!
            )
    }

    private fun updateContracts(
        contract: EndAwardPeriodData.Contract,
        contracts: Collection<Contract>
    ): List<Contract> {
        val contractsById: Map<String, Contract> = contracts.associateBy { it.id!! }

        val updatedContract = contractsById[contract.id]
            ?.let {
                it.copy(
                    status = contract.status,
                    statusDetails = contract.statusDetails,
                    milestones = createMilestones(milestones = contract.milestones)
                )
            }
            ?: throw ErrorException(ErrorType.CONTRACT_NOT_FOUND)

        return contractsById.map { (id, value) ->
            if (id == contract.id)
                updatedContract
            else
                value
        }
    }

    private fun createMilestones(milestones: List<EndAwardPeriodData.Contract.Milestone>): List<Milestone> {
        return milestones.map { milestone ->
            Milestone(
                id = milestone.id,
                title = milestone.title,
                description = milestone.description,
                type = milestone.type,
                status = milestone.status,
                relatedItems = milestone.relatedItems,
                additionalInformation = milestone.additionalInformation,
                dueDate = milestone.dueDate,
                relatedParties = milestone.relatedParties.map { relatedParty ->
                    RelatedParty(
                        id = relatedParty.id,
                        name = relatedParty.name
                    )
                },
                dateModified = milestone.dateModified,
                dateMet = milestone.dateMet
            )
        }
    }

    private fun updateBids(data: EndAwardPeriodData, bids: Bids?): Bids? {
        if (bids?.details == null) return bids

        val bidsFromRequestById: Map<String, EndAwardPeriodData.Bid> = data.bids?.associateBy { it.id } ?: emptyMap()
        if (bidsFromRequestById.isEmpty()) return bids

        val updatedBids = bids.details.map { bid ->
            bidsFromRequestById[bid.id]
                ?.let {
                    bid.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: bid
        }
        return bids.copy(
            details = updatedBids
        )
    }

    private fun updateAwards(data: EndAwardPeriodData, awards: Collection<Award>?): List<Award> {
        if (awards == null || awards.isEmpty()) return emptyList()

        val awardsFromRequestById: Map<String, EndAwardPeriodData.Award> = data.awards.associateBy { it.id }
        return awards.map { award ->
            awardsFromRequestById[award.id]
                ?.let {
                    award.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: award
        }
    }

    private fun updateLots(data: EndAwardPeriodData, lots: Collection<Lot>?): List<Lot> {
        if (lots == null || lots.isEmpty()) return emptyList()

        val lotsFromRequestById: Map<String, EndAwardPeriodData.Lot> = data.lots.associateBy { it.id }
        return lots.map { lot ->
            lotsFromRequestById[lot.id]
                ?.let {
                    lot.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: lot
        }
    }

    private fun updateCanContracts(data: EndAwardPeriodData, contracts: Collection<Contract>?): List<Contract> {
        if (contracts == null || contracts.isEmpty()) return emptyList()

        val cansFromRequestById: Map<String, EndAwardPeriodData.CAN> = data.cans.associateBy { it.id }
        return contracts.map { contract ->
            cansFromRequestById[contract.id]
                ?.let {
                    contract.copy(
                        status = it.status,
                        statusDetails = it.statusDetails
                    )
                }
                ?: contract
        }
    }

    override fun consider(context: AwardConsiderationContext, data: AwardConsiderationData) {
        val recordEntity = releaseService.getRecordEntity(cpId = context.cpid, ocId = context.ocid)
        val release = releaseService.getRelease(recordEntity.jsonData)

        val updatedBids = release.bids?.details?.map { dbBid ->
            val bidId = BidId.fromString(dbBid.id)
            if (bidId == data.bid.id) {
                dbBid.copy(
                    documents = data.bid.documents
                        .map { document ->
                            Document(
                                id = document.id,
                                description = document.description,
                                title = document.title,
                                documentType = document.documentType.toString(),
                                relatedLots = document.relatedLots.map { it.toString() },
                                url = document.url,
                                datePublished = document.datePublished,
                                language = null,
                                dateModified = null,
                                format = null,
                                relatedConfirmations = null
                            )
                        }
                )
            } else {
                dbBid
            }
        }

        val updatedRecord = release.copy(
            id = generationService.generateReleaseId(ocid = context.ocid),
            date = context.releaseDate,
            tag = listOf(Tag.AWARD_UPDATE),
            awards = release.awards
                .map { award ->
                    val requestAward = data.award
                    if (award.id == requestAward.id.toString())
                        award.copy(statusDetails = requestAward.statusDetails.value)
                    else
                        award
                },
            bids = release.bids?.copy(details = updatedBids)
        )

        releaseService.saveRecord(
            cpId = context.cpid,
            stage = context.stage,
            release = updatedRecord,
            publishDate = recordEntity.publishDate
        )
    }
}
