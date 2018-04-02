package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.procurement.notice.model.ocds.Bid;
import com.procurement.notice.model.ocds.Bids;
import com.procurement.notice.model.ocds.Tender;
import com.procurement.notice.model.tender.record.RecordTender;
import java.util.List;
import lombok.Getter;

@Getter
public class StartNewStageDto {

    @JsonProperty("tender")
    private final RecordTender tender;

    @JsonProperty("bids")
    private final Bids bids;

    @JsonCreator
    public StartNewStageDto(@JsonProperty("tender") final RecordTender tender,
                            @JsonProperty("bids") final Bids bids) {
        this.tender = tender;
        this.bids = bids;
    }
}
