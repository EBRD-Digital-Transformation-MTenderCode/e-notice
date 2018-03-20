package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.procurement.notice.model.ocds.Bid;
import com.procurement.notice.model.ocds.Lot;
import com.procurement.notice.model.ocds.Tender;
import java.util.List;
import lombok.Getter;

@Getter
public class StartNewStageDto {

    @JsonProperty("lots")
    private final List<Lot> lots;
    @JsonProperty("bids")
    private final List<Bid> bids;
    @JsonProperty("tender")
    private final Tender tender;

    @JsonCreator
    public StartNewStageDto(@JsonProperty("lots") final List<Lot> lots,
                            @JsonProperty("bids") final List<Bid> bids,
                            @JsonProperty("tender") final Tender tender) {
        this.lots = lots;
        this.bids = bids;
        this.tender = tender;
    }
}
