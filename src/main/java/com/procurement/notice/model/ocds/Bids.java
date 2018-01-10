package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import java.util.List;
import lombok.Getter;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "statistics",
        "details"
})
public class Bids {
    @JsonProperty("statistics")
    @JsonPropertyDescription("Summary statistics on the number and nature of bids received. Where information is " +
            "provided on individual bids, these statistics should match those that can be calculated from the bid " +
            "details" +
            " array.")
    private final List<BidsStatistic> statistics;

    @JsonProperty("details")
    @JsonPropertyDescription("An array of bids, providing information on the bidders, and where applicable, bid " +
            "status, bid values and related documents. The extent to which this information can be disclosed varies " +
            "from " +
            "jurisdiction to jurisdiction.")
    private final List<Bid> details;


    @JsonCreator
    public Bids(@JsonProperty("statistics") final List<BidsStatistic> statistics,
                @JsonProperty("details") final List<Bid> details) {
        this.statistics = statistics;
        this.details = details;
    }

}
