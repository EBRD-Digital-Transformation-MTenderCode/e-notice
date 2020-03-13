package com.procurement.notice.service

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
import com.procurement.notice.model.tender.ms.Ms
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
                    roles = setOf(PartyRole.BUYER).toHashSet(),
                    details = buyer.details,
                    buyerProfile = buyer.buyerProfile,
                    persones = null
            )
            if (ei.parties == null) ei.parties = hashSetOf()
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
                    roles = setOf(PartyRole.FUNDER).toHashSet(),
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
                        roles = setOf(PartyRole.PAYER).toHashSet(),
                        details = payer.details,
                        buyerProfile = null,
                        persones = null
                )
                fs.parties.add(partyPayer)
            }
            fs.payer = null
        }
    }

    fun processMsParties(ms: Ms, checkFs: CheckFsDto) {
        ms.parties.let { parties ->
            if (checkFs.buyer.isNotEmpty()) checkFs.buyer.forEach { buyer -> addParty(parties = parties, organization = buyer, role = PartyRole.BUYER) }
            if (checkFs.payer.isNotEmpty()) checkFs.payer.forEach { payer -> addParty(parties = parties, organization = payer, role = PartyRole.PAYER) }
            if (checkFs.funder.isNotEmpty()) checkFs.funder.forEach { funder -> addParty(parties = parties, organization = funder, role = PartyRole.FUNDER) }
            ms.tender.procuringEntity?.let { procuringEntity ->
                addParty(parties = parties, organization = procuringEntity, role = PartyRole.PROCURING_ENTITY)
                clearOrganizationReference(procuringEntity)
            }
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
                                               funders: HashSet<OrganizationReference>?,
                                               payers: HashSet<OrganizationReference>?) {
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

    private fun addParty(parties: MutableList<Organization>, organization: OrganizationReference?, role: PartyRole) {
        if (organization != null) {
            organization.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
            val partyPresent = getParty(parties, organization.id)
            if (partyPresent != null) {
                partyPresent.roles.add(role)
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
                        roles = setOf(role).toHashSet(),
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
                partyPresent.roles.add(role)
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
                        roles = setOf(role).toHashSet(),
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
