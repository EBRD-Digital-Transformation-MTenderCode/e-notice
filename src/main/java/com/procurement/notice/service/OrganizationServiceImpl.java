package com.procurement.notice.service;

import com.procurement.notice.model.budget.ReleaseEI;
import com.procurement.notice.model.budget.ReleaseFS;
import com.procurement.notice.model.ocds.Organization;
import com.procurement.notice.model.ocds.OrganizationReference;
import com.procurement.notice.model.tender.dto.CheckFsDto;
import com.procurement.notice.model.tender.ms.Ms;
import java.util.*;
import org.springframework.stereotype.Service;

@Service
public class OrganizationServiceImpl implements OrganizationService {


    public void processEiParties(final ReleaseEI ei) {
        final OrganizationReference buyer = ei.getBuyer();
        if (Objects.nonNull(buyer)) {
            final Organization partyBuyer = new Organization(
                    buyer.getId(),
                    buyer.getName(),
                    buyer.getIdentifier(),
                    new LinkedHashSet(buyer.getAdditionalIdentifiers()),
                    buyer.getAddress(),
                    buyer.getContactPoint(),
                    new HashSet(Arrays.asList(Organization.PartyRole.BUYER)),
                    buyer.getDetails(),
                    buyer.getBuyerProfile()
            );
            if (Objects.isNull(ei.getParties())) {
                ei.setParties(new LinkedHashSet<>());
            }
            ei.getParties().add(partyBuyer);
            clearOrganizationReference(buyer);
        }
    }

    public void processFsParties(final ReleaseFS fs) {
        /*funder*/
        final OrganizationReference funder = fs.getFunder();
        if (Objects.nonNull(funder)) {
            final Organization partyFunder = new Organization(
                    funder.getId(),
                    funder.getName(),
                    funder.getIdentifier(),
                    new LinkedHashSet(funder.getAdditionalIdentifiers()),
                    funder.getAddress(),
                    funder.getContactPoint(),
                    new HashSet(Arrays.asList(Organization.PartyRole.FUNDER)),
                    null,
                    null
            );
            if (Objects.isNull(fs.getParties())) {
                fs.setParties(new LinkedHashSet<>());
            }
            fs.getParties().add(partyFunder);
            fs.setFunder(null);
        }
       /*payer*/
        final OrganizationReference payer = fs.getPayer();
        if (Objects.nonNull(payer)) {
            final Optional<Organization> partyOptional = getParty(fs.getParties(), payer.getId());
            final Organization partyPayer;
            if (partyOptional.isPresent()) {
                partyPayer = partyOptional.get();
                partyPayer.getRoles().add(Organization.PartyRole.PAYER);
            } else {
                partyPayer = new Organization(
                        payer.getId(),
                        payer.getName(),
                        payer.getIdentifier(),
                        new LinkedHashSet(payer.getAdditionalIdentifiers()),
                        payer.getAddress(),
                        payer.getContactPoint(),
                        new HashSet(Arrays.asList(Organization.PartyRole.PAYER)),
                        null,
                        null
                );
                if (Objects.isNull(fs.getParties())) {
                    fs.setParties(new LinkedHashSet<>());
                }
                fs.getParties().add(partyPayer);
            }
            fs.setPayer(null);
        }
    }

    @Override
    public void processMsParties(Ms ms, CheckFsDto checkFs) {
        final Set<Organization> parties = ms.getParties();
        addParty(parties, ms.getTender().getProcuringEntity(), Organization.PartyRole.PROCURING_ENTITY);
        clearOrganizationReference(ms.getTender().getProcuringEntity());
        checkFs.getBuyer().forEach(buyer -> addParty(parties, buyer, Organization.PartyRole.BUYER));
        checkFs.getFunder().forEach(funder -> addParty(parties, funder, Organization.PartyRole.FUNDER));
        checkFs.getPayer().forEach(payer -> addParty(parties, payer, Organization.PartyRole.PAYER));
    }

    private void addParty(final Set<Organization> parties,
                          final OrganizationReference organization,
                          final Organization.PartyRole role) {
        if (Objects.nonNull(organization)) {
            final Optional<Organization> partyOptional = getParty(parties, organization.getId());
            final Organization party;
            if (partyOptional.isPresent()) {
                party = partyOptional.get();
                party.getRoles().add(role);
            } else {
                party = new Organization(
                        organization.getId(),
                        organization.getName(),
                        organization.getIdentifier(),
                        new LinkedHashSet(organization.getAdditionalIdentifiers()),
                        organization.getAddress(),
                        organization.getContactPoint(),
                        new HashSet(Arrays.asList(role)),
                        organization.getDetails(),
                        organization.getBuyerProfile()
                );
                parties.add(party);
            }
        }
    }

    private Optional<Organization> getParty(final Set<Organization> parties, final String partyId) {
        Optional<Organization> organizationOptional = Optional.empty();
        if (Objects.nonNull(parties))
            organizationOptional = parties.stream().filter(p -> p.getId().equals(partyId)).findFirst();
        return organizationOptional;
    }

    private void clearOrganizationReference(final OrganizationReference organization) {
        if (Objects.nonNull(organization)) {
            organization.setIdentifier(null);
            organization.setAdditionalIdentifiers(null);
            organization.setAddress(null);
            organization.setContactPoint(null);
            organization.setDetails(null);
            organization.setBuyerProfile(null);
        }
    }
}
