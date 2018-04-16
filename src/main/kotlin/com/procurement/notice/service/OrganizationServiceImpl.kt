package com.procurement.notice.service

import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.ocds.*
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Record
import org.springframework.stereotype.Service

@Service
interface OrganizationService {

    fun processEiParties(ei: EI)

    fun processFsParties(fs: FS)

    fun processMsParties(ms: Ms, checkFs: CheckFsDto)

    fun processMsPartiesFromBids(record: Record, bids: Bids)

    fun processRecordPartiesFromAwards(record: Record, awards: List<Award>)

    fun processRecordPartiesFromBids(record: Record, tenderers: List<OrganizationReference>)
}

@Service
class OrganizationServiceImpl : OrganizationService {

    override fun processEiParties(ei: EI) {
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
            if (ei.parties == null) ei.parties = hashSetOf()
            ei.parties?.add(partyBuyer)
            clearOrganizationReference(buyer)
        }
    }

    override fun processFsParties(fs: FS) {
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
            if (fs.parties == null) fs.parties = hashSetOf()
            fs.parties?.add(partyFunder)
            fs.funder = null
        }
        val payer = fs.payer
        if (payer != null) {
            val partyPresent = fs.parties?.let { getParty(fs.parties!!, payer.id!!) }
            if (partyPresent != null) {
                partyPresent.roles.add(PartyRole.PAYER)
            } else {
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
                if (fs.parties == null) fs.parties = hashSetOf()
                fs.parties?.add(partyPayer)
            }
            fs.payer = null
        }
    }

    override fun processMsParties(ms: Ms, checkFs: CheckFsDto) {
        if (ms.parties == null) ms.parties = hashSetOf()
        addParty(ms.parties!!, ms.tender.procuringEntity!!, PartyRole.PROCURING_ENTITY)
        clearOrganizationReference(ms.tender.procuringEntity!!)
        checkFs.buyer.forEach { addParty(ms.parties!!, it, PartyRole.BUYER) }
        checkFs.payer.forEach { addParty(ms.parties!!, it, PartyRole.PAYER) }
        checkFs.funder.forEach { addParty(ms.parties!!, it, PartyRole.FUNDER) }
    }

    private fun addParty(parties: HashSet<Organization>, organization: OrganizationReference, role: PartyRole) {
        val partyPresent = getParty(parties, organization.id!!)
        if (partyPresent != null) partyPresent.roles.add(role)
        else {
            val additionalIdentifiers = organization.additionalIdentifiers?.let {
                HashSet(organization.additionalIdentifiers)
            }
            val party = Organization(
                    id = organization.id,
                    name = organization.name,
                    identifier = organization.identifier,
                    additionalIdentifiers = additionalIdentifiers,
                    address = organization.address,
                    contactPoint = organization.contactPoint,
                    roles = setOf(role).toHashSet(),
                    details = organization.details,
                    buyerProfile = organization.buyerProfile
            )
            parties.add(party)
        }
    }

    override fun processMsPartiesFromBids(record: Record, bids: Bids) {
        if (record.parties == null) record.parties = hashSetOf()
        bids.details!!.asSequence()
                .flatMap { it.tenderers!!.asSequence() }
                .forEach { addParty(record.parties!!, it, PartyRole.TENDERER) }
    }

    override fun processRecordPartiesFromAwards(record: Record, awards: List<Award>) {
        if (record.parties == null) record.parties = hashSetOf()
        awards.asSequence()
                .flatMap { it.suppliers!!.asSequence() }
                .forEach { addParty(record.parties!!, it, PartyRole.SUPPLIER) }
    }

    override fun processRecordPartiesFromBids(record: Record, tenderers: List<OrganizationReference>) {
        if (record.parties == null) record.parties = hashSetOf()
        tenderers.asSequence()
                .forEach { addParty(record.parties!!, it, PartyRole.TENDERER) }
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
