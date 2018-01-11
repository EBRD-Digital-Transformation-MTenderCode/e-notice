package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "reducesTimeLimits",
        "isACallForCompetition",
        "socialOrOtherSpecificServices"
})
public class PurposeOfNotice {
    @JsonProperty("reducesTimeLimits")
    @JsonPropertyDescription("A True/False field to indicate whether this notice aims at reducing time limits for the" +
            " receipt of tenders ")
    private final Boolean reducesTimeLimits;

    @JsonProperty("isACallForCompetition")
    @JsonPropertyDescription("A True/False field to indicate whether this notice is a call for competition")
    private final Boolean isACallForCompetition;

    @JsonProperty("socialOrOtherSpecificServices")
    @JsonPropertyDescription("A True/False field to indicate whether this notice is for social or other specific " +
            "services")
    private final Boolean socialOrOtherSpecificServices;

    @JsonCreator
    public PurposeOfNotice(@JsonProperty("reducesTimeLimits") final Boolean reducesTimeLimits,
                           @JsonProperty("isACallForCompetition") final Boolean isACallForCompetition,
                           @JsonProperty("socialOrOtherSpecificServices") final Boolean socialOrOtherSpecificServices) {
        super();
        this.reducesTimeLimits = reducesTimeLimits;
        this.isACallForCompetition = isACallForCompetition;
        this.socialOrOtherSpecificServices = socialOrOtherSpecificServices;
    }
}
