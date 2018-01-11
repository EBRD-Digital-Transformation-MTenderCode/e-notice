package com.procurement.notice.model.ocds;

import com.fasterxml.jackson.annotation.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "maximumLotsBidPerSupplier",
        "maximumLotsAwardedPerSupplier"
})
public class LotDetails {
    @JsonProperty("maximumLotsBidPerSupplier")
    @JsonPropertyDescription("The maximum number of lots that one supplier may bid for as part of this contracting " +
            "process.")
    private final Integer maximumLotsBidPerSupplier;

    @JsonProperty("maximumLotsAwardedPerSupplier")
    @JsonPropertyDescription("The maximum number of lots that may be awarded to one supplier as part of this " +
            "contracting process.")
    private final Integer maximumLotsAwardedPerSupplier;

    @JsonCreator
    public LotDetails(@JsonProperty("maximumLotsBidPerSupplier") final Integer maximumLotsBidPerSupplier,
                      @JsonProperty("maximumLotsAwardedPerSupplier") final Integer maximumLotsAwardedPerSupplier) {
        this.maximumLotsBidPerSupplier = maximumLotsBidPerSupplier;
        this.maximumLotsAwardedPerSupplier = maximumLotsAwardedPerSupplier;
    }
}
