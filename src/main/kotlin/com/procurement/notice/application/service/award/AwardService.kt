package com.procurement.notice.application.service.award

import com.procurement.notice.model.ocds.Address
import com.procurement.notice.model.ocds.AddressDetails
import com.procurement.notice.model.ocds.Award
import com.procurement.notice.model.ocds.ContactPoint
import com.procurement.notice.model.ocds.CountryDetails
import com.procurement.notice.model.ocds.Details
import com.procurement.notice.model.ocds.Identifier
import com.procurement.notice.model.ocds.LocalityDetails
import com.procurement.notice.model.ocds.Lot
import com.procurement.notice.model.ocds.Option
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.ocds.Period
import com.procurement.notice.model.ocds.PlaceOfPerformance
import com.procurement.notice.model.ocds.RecurrentProcurement
import com.procurement.notice.model.ocds.RegionDetails
import com.procurement.notice.model.ocds.Renewal
import com.procurement.notice.model.ocds.Stage
import com.procurement.notice.model.ocds.Tag
import com.procurement.notice.model.ocds.Value
import com.procurement.notice.model.ocds.Variant
import com.procurement.notice.service.ReleaseService
import com.procurement.notice.utils.toDate
import org.springframework.stereotype.Service

interface AwardService {
    fun createAward(context: CreateAwardContext, data: CreateAwardData)
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

        //BR-2.6.18.7
        val updatedLots = updateLots(
            requestLots = data.lots ?: emptyList(),
            tenderLots = tender.lots ?: emptyList()
        )

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

            tender = tender.copy(
                lots = updatedLots.toHashSet()
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

    /** BR-2.6.18.7 Lots
     *
     * 1. IF в payload запроса присутствует секция Lots при operationType = "awardCreate",
     *       то в массив Lots нового релиза eNotice должен поместить тот объект Lot,
     *       который был получен в запросе на создание нового релиза. Шаги eNotice:
     *   a. eNotice копирует из актуального релиза NP Record в секцию Lots (tender/lots) нового релиза
     *      все сохраненные ранее объекты.
     *   b. eNotice производит поиск в массиве Lots формируемого релиза по значению "ID" (lots/id) лота
     *      из запроса на создание релиза.
     *   c. eNotice перезаписывает найденный Lot. Данные для сохранения передаются в объекте Lots в payload запроса.
     * 2. ELSE
     *      в payload запроса НЕТ секции Lots при operationType = "awardCreate"",
     *      секция Lots формируется на основании данных из актуального релиза Negotiation Record без изменений.
     */
    private fun updateLots(requestLots: List<CreateAwardData.Lot>, tenderLots: Collection<Lot>): Collection<Lot> {
        val requestLotsById = requestLots.associateBy({ it.id }, { convertLot(it) })
        if (requestLotsById.isEmpty())
            return tenderLots

        return if (tenderLots.isNotEmpty())
            tenderLots.map { tenderLot ->
                requestLotsById[tenderLot.id] ?: tenderLot
            }
        else
            requestLotsById.values
    }

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

    private fun convertLot(lot: CreateAwardData.Lot): Lot = Lot(
        id = lot.id,
        title = lot.title,
        description = lot.description,
        status = lot.status,
        statusDetails = lot.statusDetails,
        value = lot.value.let { value ->
            Value(
                amount = value.amount,
                currency = value.currency,
                amountNet = null,
                valueAddedTaxIncluded = null
            )
        },
        options = lot.options.map { option ->
            Option(
                hasOptions = option.hasOptions,
                optionDetails = null
            )
        },
        recurrentProcurement = lot.recurrentProcurement.map { recurrentProcurement ->
            RecurrentProcurement(
                isRecurrent = recurrentProcurement.isRecurrent,
                dates = null,
                description = null
            )
        },
        renewals = lot.renewals.map { renewal ->
            Renewal(
                hasRenewals = renewal.hasRenewals,
                maxNumber = null,
                renewalConditions = null
            )
        },
        variants = lot.variants.map { variant ->
            Variant(
                hasVariants = variant.hasVariants,
                variantDetails = null
            )
        },
        contractPeriod = lot.contractPeriod.let { contractPeriod ->
            Period(
                startDate = contractPeriod.startDate,
                endDate = contractPeriod.endDate,
                maxExtentDate = null,
                durationInDays = null
            )
        },
        placeOfPerformance = lot.placeOfPerformance.let { placeOfPerformance ->
            PlaceOfPerformance(
                description = placeOfPerformance.description,
                address = placeOfPerformance.address.let { address ->
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
                nutScode = null
            )
        }
    )

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
}