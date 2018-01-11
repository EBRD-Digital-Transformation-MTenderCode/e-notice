package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.LinkedHashSet;
import java.util.Set;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "hasPrizes",
        "prizes",
        "paymentsToParticipants",
        "serviceContractAward",
        "juryDecisionBinding",
        "juryMembers",
        "participants"
})
public class DesignContest {
    @JsonProperty("hasPrizes")
    @JsonPropertyDescription("A True/False field to indicate if the design contest has prizes. Required by the EU")
    private final Boolean hasPrizes;

    @JsonProperty("prizes")
    @JsonPropertyDescription("A good, service, or work to be contracted.")
    private final Set<Item> prizes;

    @JsonProperty("paymentsToParticipants")
    @JsonPropertyDescription("Details of any payments that will be made to participants in the design contest. " +
            "Required by the EU")
    private final String paymentsToParticipants;

    @JsonProperty("serviceContractAward")
    @JsonPropertyDescription("A True/False field to indicate whether a service contract will be awarded to the winner" +
            "(s) of the design contest. Required by the EU")
    private final Boolean serviceContractAward;

    @JsonProperty("juryDecisionBinding")
    @JsonPropertyDescription("A True/False field to indicate whether the jury decision of the design contest is " +
            "binding. Required by the EU")
    private final Boolean juryDecisionBinding;

    @JsonProperty("juryMembers")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("A list of the jury members for of the design contest. Required by the EU")
    private final Set<OrganizationReference> juryMembers;

    @JsonProperty("participants")
    @JsonDeserialize(as = LinkedHashSet.class)
    @JsonPropertyDescription("A list of the pre-selected participants for the design contest. Required by the EU")
    private final Set<OrganizationReference> participants;

    @JsonCreator
    public DesignContest(@JsonProperty("hasPrizes") final Boolean hasPrizes,
                         @JsonProperty("prizes") final LinkedHashSet<Item> prizes,
                         @JsonProperty("paymentsToParticipants") final String paymentsToParticipants,
                         @JsonProperty("serviceContractAward") final Boolean serviceContractAward,
                         @JsonProperty("juryDecisionBinding") final Boolean juryDecisionBinding,
                         @JsonProperty("juryMembers") final LinkedHashSet<OrganizationReference> juryMembers,
                         @JsonProperty("participants") final LinkedHashSet<OrganizationReference> participants) {
        this.hasPrizes = hasPrizes;
        this.prizes = prizes;
        this.paymentsToParticipants = paymentsToParticipants;
        this.serviceContractAward = serviceContractAward;
        this.juryDecisionBinding = juryDecisionBinding;
        this.juryMembers = juryMembers;
        this.participants = participants;
    }
}
