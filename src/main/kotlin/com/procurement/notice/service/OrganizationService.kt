package com.procurement.notice.service

import com.procurement.notice.application.service.tender.periodEnd.TenderPeriodEndContext
import com.procurement.notice.domain.extention.toList
import com.procurement.notice.domain.model.ProcurementMethod
import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.contract.ContractRecord
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.enquiry.RecordEnquiry
import com.procurement.notice.model.tender.ms.MsTender
import com.procurement.notice.model.tender.record.Release
import org.springframework.stereotype.Service

@Service
class OrganizationService {

    fun processEiParties(ei: EI) {
        val buyer = ei.buyer
        if (buyer != null) {
            val partyBuyer = Organization(
                id = buyer.id,
                name = buyer.name,
                identifier = buyer.identifier,
                additionalIdentifiers = buyer.additionalIdentifiers,
                address = buyer.address,
                contactPoint = buyer.contactPoint,
                roles = mutableListOf(PartyRole.BUYER),
                details = buyer.details,
                buyerProfile = buyer.buyerProfile,
                persones = null
            )
            if (ei.parties == null) ei.parties = mutableListOf()
            ei.parties?.add(partyBuyer)
            clearOrganizationReference(buyer)
            if (ei.parties != null && ei.parties!!.isEmpty()) ei.parties = null
        }
    }

    fun processFsParties(fs: FS) {
        val funder = fs.funder
        if (funder != null) {
            val partyFunder = Organization(
                id = funder.id,
                name = funder.name,
                identifier = funder.identifier,
                additionalIdentifiers = funder.additionalIdentifiers,
                address = funder.address,
                contactPoint = funder.contactPoint,
                roles = mutableListOf(PartyRole.FUNDER),
                details = funder.details,
                buyerProfile = null,
                persones = null
            )
            fs.parties.add(partyFunder)
            fs.funder = null
        }
        val payer = fs.payer
        if (payer != null) {
            val payerId = payer.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
            val partyPresent = fs.parties.let { getParty(parties = it, partyId = payerId) }
            if (partyPresent != null) {
                partyPresent.roles.add(PartyRole.PAYER)
            } else {
                val partyPayer = Organization(
                    id = payer.id,
                    name = payer.name,
                    identifier = payer.identifier,
                    additionalIdentifiers = payer.additionalIdentifiers,
                    address = payer.address,
                    contactPoint = payer.contactPoint,
                    roles = mutableListOf(PartyRole.PAYER),
                    details = payer.details,
                    buyerProfile = null,
                    persones = null
                )
                fs.parties.add(partyPayer)
            }
            fs.payer = null
        }
    }

    fun processRecordPartiesFromBids(release: Release) {
        release.bids?.details?.let { bids ->
            bids.forEach { bid ->
                bid.tenderers?.let { tenderers ->
                    tenderers.forEach { tenderer ->
                        release.parties.let { addParty(parties = it, organization = tenderer, role = PartyRole.TENDERER) }
                        clearOrganizationReference(tenderer)
                    }
                }
            }
        }
    }

    fun processRecordPartiesFromAwards(release: Release) {
        release.awards.let { awards ->
            awards.forEach { award ->
                award.suppliers?.let { suppliers ->
                    suppliers.forEach { supplier ->
                        release.parties.let { addParty(parties = it, organization = supplier, role = PartyRole.SUPPLIER) }
                        clearOrganizationReference(supplier)
                    }
                }
            }
        }
    }

    fun processRecordPartiesFromAwards(release: Release, context: TenderPeriodEndContext) {
        release.awards.let { awards ->
            awards.forEach { award ->
                award.suppliers?.let { suppliers ->
                    suppliers.forEach { supplier ->
                        val role = defineRole(context)
                        release.parties.let { addParty(parties = it, organization = supplier, role = role) }
                        clearOrganizationReference(supplier)
                    }
                }
            }
        }
    }

    private fun defineRole(context: TenderPeriodEndContext): PartyRole? =
        when (context.pmd) {
            ProcurementMethod.CF, ProcurementMethod.TEST_CF,
            ProcurementMethod.OF, ProcurementMethod.TEST_OF -> null

            ProcurementMethod.GPA, ProcurementMethod.TEST_GPA,
            ProcurementMethod.MV, ProcurementMethod.TEST_MV,
            ProcurementMethod.OT, ProcurementMethod.TEST_OT,
            ProcurementMethod.RFQ, ProcurementMethod.TEST_RFQ,
            ProcurementMethod.RT, ProcurementMethod.TEST_RT,
            ProcurementMethod.SV, ProcurementMethod.TEST_SV -> PartyRole.SUPPLIER

            ProcurementMethod.CD, ProcurementMethod.TEST_CD,
            ProcurementMethod.DA, ProcurementMethod.TEST_DA,
            ProcurementMethod.DC, ProcurementMethod.TEST_DC,
            ProcurementMethod.IP, ProcurementMethod.TEST_IP,
            ProcurementMethod.NP, ProcurementMethod.TEST_NP,
            ProcurementMethod.OP, ProcurementMethod.TEST_OP,
            ProcurementMethod.FA, ProcurementMethod.TEST_FA -> throw ErrorException(ErrorType.INVALID_PMD)
        }

    fun processContractRecordPartiesFromAwards(record: ContractRecord) {
        record.awards?.let { awards ->
            awards.forEach { award ->
                award.suppliers?.let { suppliers ->
                    suppliers.forEach { supplier ->
                        addContractParty(parties = record.parties, organization = supplier, role = PartyRole.SUPPLIER)
                        addContractParty(parties = record.parties, organization = supplier, role = PartyRole.PAYEE)
                        clearOrganizationReference(supplier)
                    }
                }
            }
        }
    }

    fun processContractRecordPartiesFromBudget(record: ContractRecord,
                                               buyer: OrganizationReference?,
                                               funders: List<OrganizationReference>?,
                                               payers: List<OrganizationReference>?) {
        buyer?.let {
            addContractParty(parties = record.parties, organization = it, role = PartyRole.BUYER)
        }
        funders?.let {
            it.forEach { funder ->
                addContractParty(parties = record.parties, organization = funder, role = PartyRole.FUNDER)
                clearOrganizationReference(funder)
            }
        }
        payers?.let {
            it.forEach { payer ->
                addContractParty(parties = record.parties, organization = payer, role = PartyRole.PAYER)
                clearOrganizationReference(payer)
            }
        }
    }

    fun processRecordPartiesFromEnquiry(release: Release, enquiry: RecordEnquiry) {
        enquiry.author?.let { author ->
            release.parties.let { addParty(parties = it, organization = author, role = PartyRole.ENQUIRER) }
            clearOrganizationReference(author)
        }
    }

    private fun addParty(parties: MutableList<Organization>, organization: OrganizationReference?, role: PartyRole?) {
        if (organization != null) {
            organization.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
            val partyPresent = getParty(parties, organization.id)
            if (partyPresent != null) {

                role?.let { partyRole ->
                    if (role !in partyPresent.roles) partyPresent.roles.add(partyRole)
                }

                if (partyPresent.name.isNullOrEmpty()) partyPresent.name = organization.name
                if (partyPresent.identifier == null) partyPresent.identifier = organization.identifier
                if (partyPresent.additionalIdentifiers == null) partyPresent.additionalIdentifiers = organization.additionalIdentifiers
                if (partyPresent.address == null) partyPresent.address = organization.address
                if (partyPresent.contactPoint == null) partyPresent.contactPoint = organization.contactPoint
                if (partyPresent.details == null) partyPresent.details = organization.details
                if (partyPresent.buyerProfile.isNullOrEmpty()) partyPresent.buyerProfile = organization.buyerProfile
                if (partyPresent.persones == null) partyPresent.persones = organization.persones
            } else {
                val party = Organization(
                    id = organization.id,
                    name = organization.name,
                    identifier = organization.identifier,
                    additionalIdentifiers = organization.additionalIdentifiers,
                    address = organization.address,
                    contactPoint = organization.contactPoint,
                    roles = role
                        ?.let { mutableListOf(it) }
                        ?: mutableListOf()
                    ,
                    details = organization.details,
                    buyerProfile = organization.buyerProfile,
                    persones = organization.persones
                )
                parties.add(party)
            }
        }
    }

    private fun addContractParty(parties: MutableList<Organization>, organization: OrganizationReference?, role: PartyRole) {
        if (organization != null) {
            organization.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
            val partyPresent = getParty(parties, organization.id)
            if (partyPresent != null) {
                if (role !in partyPresent.roles) partyPresent.roles.add(role)
                if (partyPresent.name.isNullOrEmpty()) partyPresent.name = organization.name
                organization.identifier?.let { partyPresent.identifier = organization.identifier }
                organization.additionalIdentifiers?.let { partyPresent.additionalIdentifiers = organization.additionalIdentifiers }
                organization.address?.let { partyPresent.address = organization.address }
                organization.contactPoint?.let { partyPresent.contactPoint = organization.contactPoint }
                organization.details?.let { partyPresent.details = organization.details }
                organization.buyerProfile?.let { partyPresent.buyerProfile = organization.buyerProfile }
                organization.persones?.let { partyPresent.persones = organization.persones }
            } else {
                val party = Organization(
                    id = organization.id,
                    name = organization.name,
                    identifier = organization.identifier,
                    additionalIdentifiers = organization.additionalIdentifiers,
                    address = organization.address,
                    contactPoint = organization.contactPoint,
                    roles = mutableListOf(role),
                    details = organization.details,
                    buyerProfile = organization.buyerProfile,
                    persones = organization.persones
                )
                parties.add(party)
            }
        }
    }

    private fun getParty(parties: List<Organization>, partyId: String): Organization? {
        return parties.asSequence().firstOrNull { it.id == partyId }
    }

    private fun clearOrganizationReference(organization: OrganizationReference) {
        organization.identifier = null
        organization.additionalIdentifiers = null
        organization.address = null
        organization.contactPoint = null
        organization.details = null
        organization.buyerProfile = null
        organization.persones = null
    }
}

fun List<Organization>.mergeParties(msTender: MsTender, checkFs: CheckFsDto): List<Organization> =
    this.mergePartiesBy(organizations = checkFs.buyer, role = PartyRole.BUYER)
        .mergePartiesBy(organizations = checkFs.payer, role = PartyRole.PAYER)
        .mergePartiesBy(organizations = checkFs.funder, role = PartyRole.FUNDER)
        .mergePartiesBy(organizations = msTender.procuringEntity.toList(), role = PartyRole.PROCURING_ENTITY)

fun List<Organization>.mergePartiesBy(
    organizations: List<OrganizationReference>,
    role: PartyRole
): List<Organization> {
    if (organizations.isEmpty()) return this

    val partiesId: List<String> = this.map { it.id!! }
    val organizationsId: List<String> = organizations.map { it.id!! }
    val allIds: List<String> = partiesId + organizationsId

    val partiesById: Map<String, Organization> = this.associateBy { it.id!! }
    val organizationsById: Map<String, OrganizationReference> = organizations.associateBy { it.id!! }

    return allIds.map { id ->
        partiesById[id]
            ?.let { party ->
                organizationsById[id]
                    ?.let { organization ->
                        Organization(
                            id = id,
                            name = if (party.name.isNullOrEmpty()) organization.name else party.name,
                            identifier = if (party.identifier == null) organization.identifier else party.identifier,
                            additionalIdentifiers = if (party.additionalIdentifiers == null) organization.additionalIdentifiers else party.additionalIdentifiers,
                            address = if (party.address == null) organization.address else party.address,
                            contactPoint = if (party.contactPoint == null) organization.contactPoint else party.contactPoint,
                            details = if (party.details == null) organization.details else party.details,
                            buyerProfile = if (party.buyerProfile.isNullOrEmpty()) organization.buyerProfile else party.buyerProfile,
                            persones = if (party.persones == null) organization.persones else party.persones,
                            roles = if (role in party.roles)
                                party.roles
                            else
                                (party.roles + role).toMutableList()
                        )
                    }
                    ?: party
            }
            ?: organizationsById.getValue(id)
                .let { organization ->
                    Organization(
                        id = id,
                        name = organization.name,
                        identifier = organization.identifier,
                        additionalIdentifiers = organization.additionalIdentifiers,
                        address = organization.address,
                        contactPoint = organization.contactPoint,
                        roles = mutableListOf(role),
                        details = organization.details,
                        buyerProfile = organization.buyerProfile,
                        persones = organization.persones
                    )
                }
    }
}
