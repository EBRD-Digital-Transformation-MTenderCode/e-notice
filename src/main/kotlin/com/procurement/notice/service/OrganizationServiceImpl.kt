package com.procurement.notice.service

import com.procurement.notice.exception.ErrorException
import com.procurement.notice.exception.ErrorType
import com.procurement.notice.model.budget.EI
import com.procurement.notice.model.budget.FS
import com.procurement.notice.model.ocds.Organization
import com.procurement.notice.model.ocds.OrganizationReference
import com.procurement.notice.model.ocds.PartyRole
import com.procurement.notice.model.tender.dto.CheckFsDto
import com.procurement.notice.model.tender.enquiry.RecordEnquiry
import com.procurement.notice.model.tender.ms.Ms
import com.procurement.notice.model.tender.record.Record
import org.springframework.stereotype.Service

interface OrganizationService {

    fun processEiParties(ei: EI)

    fun processFsParties(fs: FS)

    fun processMsParties(ms: Ms, checkFs: CheckFsDto)

    fun processRecordPartiesFromBids(record: Record)

    fun processRecordPartiesFromAwards(record: Record)

    fun processRecordPartiesFromEnquiry(record: Record, enquiry: RecordEnquiry)
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
                    additionalIdentifiers = buyer.additionalIdentifiers,
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
                    additionalIdentifiers = funder.additionalIdentifiers,
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
                        additionalIdentifiers = payer.additionalIdentifiers,
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
        if (ms.tender.procuringEntity != null) {
            addParty(ms.parties!!, ms.tender.procuringEntity!!, PartyRole.PROCURING_ENTITY)
            clearOrganizationReference(ms.tender.procuringEntity!!)
        }
        if (checkFs.buyer.isNotEmpty()) checkFs.buyer.forEach { addParty(ms.parties!!, it, PartyRole.BUYER) }
        if (checkFs.payer.isNotEmpty()) checkFs.payer.forEach { addParty(ms.parties!!, it, PartyRole.PAYER) }
        if (checkFs.funder.isNotEmpty()) checkFs.funder.forEach { addParty(ms.parties!!, it, PartyRole.FUNDER) }
    }

    override fun processRecordPartiesFromBids(record: Record) {
        if (record.parties == null) record.parties = hashSetOf()
        if (record.bids?.details != null) {
            record.bids!!.details!!.asSequence()
                    .filter { it.tenderers != null }
                    .flatMap { it.tenderers!!.asSequence() }
                    .forEach {
                        addParty(record.parties!!, it, PartyRole.TENDERER)
                        clearOrganizationReference(it)
                    }
        }
    }

    override fun processRecordPartiesFromAwards(record: Record) {
        if (record.parties == null) record.parties = hashSetOf()
        if (record.awards != null) {
            record.awards!!.asSequence()
                    .filter { it.suppliers != null }
                    .flatMap { it.suppliers!!.asSequence() }
                    .forEach {
                        addParty(record.parties!!, it, PartyRole.SUPPLIER)
                        clearOrganizationReference(it)
                    }
        }
    }

    override fun processRecordPartiesFromEnquiry(record: Record, enquiry: RecordEnquiry) {
        if (record.parties == null) record.parties = hashSetOf()
        if (enquiry.author != null) {
            addParty(record.parties!!, enquiry.author, PartyRole.ENQUIRER)
            clearOrganizationReference(enquiry.author)
        }
    }

    private fun addParty(parties: HashSet<Organization>, organization: OrganizationReference?, role: PartyRole) {
        if (organization != null) {
            organization.id ?: throw ErrorException(ErrorType.PARAM_ERROR)
            val partyPresent = getParty(parties, organization.id)
            if (partyPresent != null) partyPresent.roles.add(role)
            else {
                val party = Organization(
                        id = organization.id,
                        name = organization.name,
                        identifier = organization.identifier,
                        additionalIdentifiers = organization.additionalIdentifiers,
                        address = organization.address,
                        contactPoint = organization.contactPoint,
                        roles = setOf(role).toHashSet(),
                        details = organization.details,
                        buyerProfile = organization.buyerProfile
                )
                parties.add(party)
            }
        }
    }

    private fun getParty(parties: HashSet<Organization>, partyId: String): Organization? {
        return parties.asSequence().firstOrNull { it.id == partyId }
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
