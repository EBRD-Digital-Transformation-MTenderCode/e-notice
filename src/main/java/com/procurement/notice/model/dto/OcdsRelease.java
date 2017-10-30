package com.procurement.notice.model.dto;

import java.util.List;
import lombok.Data;

@Data
public class OcdsRelease {
    public String ocid;
    public String id;
    public String date;
    public ReleaseTag tag;
    public InitiationType initiationType;
    public String title;
    public String description;
    public Organization parties;
    public Planning planning;
    public Tender tender;
    public OrganizationReference buyer;
    public List<Award> awards;
    public List<Contract> contracts;
    public String language;
    public RelatedProcess relatedProcesses;
    public Bids bids;
    public String organization;
    public String buyerInternalReferenceId;
    public Boolean hasPreviousNotice;
    public PurposeOfNotice purposeOfNotice;
    public RelatedNotice relatedNotice;
}
