package com.procurement.notice.dto;

import java.util.List;
import lombok.Data;

@Data
public class DesignContest {
    public Boolean hasPrizes;
    public List<Item> prizes;
    public String paymentsToParticipants;
    public Boolean serviceContractAward;
    public Boolean juryDecisionBinding;
    public List<OrganizationReference> juryMembers;
    public List<OrganizationReference> participants;
}
