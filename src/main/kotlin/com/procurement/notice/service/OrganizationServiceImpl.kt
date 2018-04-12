package com.procurement.notice.service

import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.ocds.Bids
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Record
import org.springframework.stereotype.Service

@Service
interface OrganizationService {

    fun processEiParties(ei: EI)

    fun processFsParties(fs: FS)

    fun processMsParties(ms: Ms, checkFs: CheckFsDto)

    fun processPartiesFromBids(record: Record, bids: Bids)
}

@Service
class OrganizationServiceImpl : OrganizationService {

    override fun processEiParties(ei: EI) {
        ei.parties ?: hashSetOf()
        val buyer = ei.buyer
        if (buyer != null) {
            val partyBuyer = Organization(
                    id = buyer.id,
                    name = buyer.name,
                    identifier = buyer.identifier,
                    additionalIdentifiers = HashSet(buyer.additionalIdentifiers),
                    address = buyer.address,
                    contactPoint = buyer.contactPoint,
                    roles = setOf(PartyRole.BUYER).toHashSet(),
                    details = buyer.details,
                    buyerProfile = buyer.buyerProfile
            )
            ei.parties?.add(partyBuyer)
            clearOrganizationReference(buyer)
        }
    }

    override fun processFsParties(fs: FS) {
        fs.parties ?: hashSetOf()
        val funder = fs.funder
        if (funder != null) {
            val partyFunder = Organization(
                    id = funder.id,
                    name = funder.name,
                    identifier = funder.identifier,
                    additionalIdentifiers = HashSet(funder.additionalIdentifiers),
                    address = funder.address,
                    contactPoint = funder.contactPoint,
                    roles = setOf(PartyRole.FUNDER).toHashSet(),
                    details = null,
                    buyerProfile = null
            )
            fs.parties?.add(partyFunder)
            fs.funder = null
        }
        val payer = fs.payer
        if (payer != null) {
            val partyPresent = getParty(fs.parties!!, payer.id!!)
            if (partyPresent != null) partyPresent.roles.add(PartyRole.PAYER)
            else {
                val partyPayer = Organization(
                        id = payer.id,
                        name = payer.name,
                        identifier = payer.identifier,
                        additionalIdentifiers = HashSet(payer.additionalIdentifiers),
                        address = payer.address,
                        contactPoint = payer.contactPoint,
                        roles = setOf(PartyRole.PAYER).toHashSet(),
                        details = null,
                        buyerProfile = null
                )
                fs.parties?.add(partyPayer)
            }
            fs.payer = null
        }
    }

    override fun processMsParties(ms: Ms, checkFs: CheckFsDto) {
        val parties = ms.parties ?: hashSetOf()
        addMsParty(parties, ms.tender.procuringEntity!!, PartyRole.PROCURING_ENTITY)
        clearOrganizationReference(ms.tender.procuringEntity!!)
        checkFs.buyer.forEach { addMsParty(parties, it, PartyRole.BUYER) }
        checkFs.payer.forEach { addMsParty(parties, it, PartyRole.PAYER) }
        checkFs.funder.forEach { addMsParty(parties, it, PartyRole.FUNDER) }
    }

    private fun addMsParty(parties: HashSet<Organization>, organization: OrganizationReference, role: PartyRole) {
        val partyPresent = getParty(parties, organization.id!!)
        if (partyPresent != null) partyPresent.roles.add(role)
        else {
            val party = Organization(
                    id = organization.id,
                    name = organization.name,
                    identifier = organization.identifier,
                    additionalIdentifiers = HashSet(organization.additionalIdentifiers!!),
                    address = organization.address,
                    contactPoint = organization.contactPoint,
                    roles = setOf(role).toHashSet(),
                    details = organization.details,
                    buyerProfile = organization.buyerProfile
            )
            parties.add(party)
        }
    }

    override fun processPartiesFromBids(record: Record, bids: Bids) {
        val parties = record.parties ?: hashSetOf()
        bids.details!!.asSequence()
                .flatMap { it.tenderers!!.asSequence() }
                .forEach { addMsParty(parties, it, PartyRole.TENDERER) }
    }

    private fun getParty(parties: HashSet<Organization>, partyId: String): Organization? {
        return parties.asSequence().filter { it.id == partyId }.firstOrNull()
    }

    private fun clearOrganizationReference(organization: OrganizationReference) {
        organization.identifier = null
        organization.additionalIdentifiers = null
        organization.address = null
        organization.contactPoint = null
        organization.details = null
        organization.buyerProfile = null
    }
}
