package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.Bid;
import com.procurement.notice.model.ocds.Lot;
import com.procurement.notice.model.ocds.Period;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

@Getter
@JsonPropertyOrder({
        "enquiry",
        "tender",
        "tenderPeriod",
        "enquiryPeriod"
})
public class StartNewStageDto {

    @JsonProperty("tender")
    @NonNull
    private final Tender tender;
    @JsonProperty("lots")
    private final List<Lot> lots;
    @JsonProperty("bids")
    private final List<Bid> bids;

    @JsonCreator
    public StartNewStageDto(@JsonProperty("tender") final Tender tender,
                            @JsonProperty("lots") final List<Lot> lots,
                            @JsonProperty("bids") final List<Bid> bids) {
        this.tender = tender;
        this.lots = lots;
        this.bids = bids;
    }

    @Getter
    public class Tender {
        @JsonProperty("tenderPeriod")
        @NonNull
        private final Period tenderPeriod;
        @JsonProperty("enquiryPeriod")
        @NonNull
        private final Period enquiryPeriod;

        @JsonCreator
        Tender(@JsonProperty("tenderPeriod") final Period tenderPeriod,
               @JsonProperty("enquiryPeriod") final Period enquiryPeriod) {
            this.tenderPeriod = tenderPeriod;
            this.enquiryPeriod = enquiryPeriod;
        }
    }

}
