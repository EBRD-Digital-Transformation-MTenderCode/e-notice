package com.procurement.notice.model.tender;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.*;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "tenderPeriod",
        "awardPeriod",
        "awards",
        "lots",
        "tenderers",
        "bids"
})
public class TenderPeriodEndDto {

    @JsonProperty("tenderPeriod")
    private final Period tenderPeriod;
    @JsonProperty("awardPeriod")
    private final Period awardPeriod;
    @JsonProperty("awards")
    private final List<Award> awards;
    @JsonProperty("lots")
    private final List<Lot> lots;
    @JsonProperty("tenderers")
    private final List<OrganizationReference> tenderers;
    @JsonProperty("bids")
    private final List<Bid> bids;

    @JsonCreator
    public TenderPeriodEndDto(@JsonProperty("tenderPeriod") final Period tenderPeriod,
                              @JsonProperty("awardPeriod") final Period awardPeriod,
                              @JsonProperty("awards") final List<Award> awards,
                              @JsonProperty("lots") final List<Lot> lots,
                              @JsonProperty("tenderers") final List<OrganizationReference> tenderers,
                              @JsonProperty("bids") final List<Bid> bids) {
        this.tenderPeriod = tenderPeriod;
        this.awardPeriod = awardPeriod;
        this.awards = awards;
        this.lots = lots;
        this.tenderers = tenderers;
        this.bids = bids;
    }
}
