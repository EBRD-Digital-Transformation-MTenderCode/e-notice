package com.procurement.notice.model.tender.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.procurement.notice.model.ocds.Award;
import com.procurement.notice.model.ocds.Bid;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "award",
        "bid"
})
public class AwardByBidDto {

    @JsonProperty("award")
    private final Award award;
    @JsonProperty("bid")
    private final Bid bid;

    @JsonCreator
    public AwardByBidDto(@JsonProperty("award") final Award award,
                         @JsonProperty("bid") final Bid bid) {
        this.award = award;
        this.bid = bid;
    }
}
